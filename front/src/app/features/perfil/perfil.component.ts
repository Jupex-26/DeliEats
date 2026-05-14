import { Component, inject, signal, OnInit, effect, untracked } from '@angular/core';
import { CommonModule, DatePipe, CurrencyPipe } from '@angular/common';
import { Router } from '@angular/router';
import { IonContent, IonIcon, IonToggle } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  personOutline,
  receiptOutline,
  arrowBackOutline,
  businessOutline,
  cameraOutline,
  bicycleOutline,
  refreshOutline,
  chatbubblesOutline,
  locationOutline
} from 'ionicons/icons';
import { AuthService } from '../../services/auth/auth-service';
import { ClienteService } from '../../services/cliente/cliente-service';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { PedidoService } from '../../services/pedido/pedido-service';
import { EstadoService } from '../../services/estado/estado-service';
import { ClienteOutputDto, RepartidorOutputDto, PedidoOutputDto, EstadoOutputDto } from '../../types';
import { PerfilEdicionComponent } from './perfil-edicion/perfil-edicion.component';
import { MisPedidosComponent } from './mis-pedidos/mis-pedidos.component';
import { EuroPipe } from '../../pipe/euro.pipe';
type Tab = 'perfil' | 'pedidos' | 'repartidor';

import { environment } from '../../../environments/environment';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';
import { ChatModalComponent } from '../../shared/chat-modal/chat-modal.component';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [
    CommonModule,
    IonContent,
    IonIcon,
    IonToggle,
    PerfilEdicionComponent,
    MisPedidosComponent,
    DatePipe,
    EuroPipe,
    InfoModalComponent,
    ChatModalComponent
  ],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  protected environment = environment;
  private authService = inject(AuthService);
  private clienteService = inject(ClienteService);
  private repartidorService = inject(RepartidorService);
  private pedidoService = inject(PedidoService);
  private estadoService = inject(EstadoService);
  private router = inject(Router);

  activeTab = signal<Tab>('perfil');
  cliente = signal<ClienteOutputDto | null>(null);
  repartidor = signal<RepartidorOutputDto | null>(null);
  loading = signal(true);
  loadingRepartidor = signal(false);

  // --- Pedidos Repartidor ---
  pedidosDisponibles = signal<PedidoOutputDto[]>([]);
  estadosList = signal<EstadoOutputDto[]>([]);
  loadingPedidos = signal(false);
  asignandoPedidoId = signal<number | null>(null);
  actualizandoEstadoPedidoId = signal<number | null>(null);
  isSuccessModalOpen = signal(false);
  successModalMessage = signal('');

  // --- Chat ---
  isChatOpen = signal(false);
  chatPedidoId = signal<number | null>(null);
  chatReceptorId = signal<number | null>(null);
  chatReceptorNombre = signal<string>('');

  constructor() {
    addIcons({
      personOutline,
      receiptOutline,
      arrowBackOutline,
      businessOutline,
      cameraOutline,
      bicycleOutline,
      refreshOutline,
      chatbubblesOutline,
      locationOutline
    });

    // effect() reacciona de forma garantizada y automática cada vez que cambia el usuario actual en el AuthService
    effect(() => {
      const user = this.authService.currentUser();
      untracked(() => {
        if (user?.userOutputDto) {
          const rol = user.userOutputDto.nombreRol;
          const id = user.userOutputDto.id;
          if (rol === 'ROLE_CLIENTE' || rol === 'ROLE_REPARTIDOR') {
            this.loading.set(true);
            this.cliente.set(null); // fuerza la destrucción de subcomponentes para evitar retención de estado local
            this.clienteService.obtenerPorId(id).subscribe({
              next: (c) => {
                this.cliente.set(c);
                this.loading.set(false);
                if (this.activeTab() === 'repartidor') {
                  this.cargarRepartidor();
                }
              },
              error: () => this.loading.set(false)
            });
          } else {
            this.loading.set(false);
          }
        } else {
          this.cliente.set(null);
          this.repartidor.set(null);
          this.pedidosDisponibles.set([]);
          this.loading.set(true);
          this.activeTab.set('perfil');
        }
      });
    });
  }

  ngOnInit() {
    /* Comentado temporalmente para depuración
    const user = this.authService.currentUser();
    if (!user?.userOutputDto) {
      this.router.navigate(['/login']);
    }
    */
  }

  get rol(): string {
    return this.authService.getRol() ?? '';
  }

  get usuario() {
    return this.authService.currentUser()?.userOutputDto ?? null;
  }

  setTab(tab: Tab) {
    this.activeTab.set(tab);
    if (tab === 'repartidor') {
      this.cargarRepartidor();
    }
  }

  cargarRepartidor() {
    const id = this.cliente()?.id;
    if (!id) return;
    this.loadingRepartidor.set(true);
    this.repartidorService.obtenerPorId(id).subscribe({
      next: (rep) => {
        this.repartidor.set(rep);
        this.loadingRepartidor.set(false);
        if (rep.disponible) {
          this.cargarEstados();
          this.cargarPedidosRepartidor();
        }
      },
      error: () => {
        this.loadingRepartidor.set(false);
      }
    });
  }

  cargarEstados() {
    if (this.estadosList().length > 0) return;
    this.estadoService.listar(0, 100).subscribe({
      next: (res) => {
        this.estadosList.set(res.content || []);
      }
    });
  }

  cargarPedidosRepartidor() {
    const currentUserId = this.usuario?.id;
    const repId = this.repartidor()?.id;
    if (!currentUserId || !repId) return;

    this.loadingPedidos.set(true);
    this.pedidoService.listar(0, 100, 'fechaCompra,desc').subscribe({
      next: (res) => {
        const todos: PedidoOutputDto[] = res.content || [];

        // Filtrado estricto:
        // 1. Excluir pedidos propios (clienteId === currentUserId)
        // 2. Mostrar pedidos sin asignar (repartidorId nulo) o asignados a este repartidor
        // 3. Omitir pedidos ya terminados (Entregado/Cancelado)
        const filtrados = todos.filter(p => {
          if (p.clienteId === currentUserId) return false;

          const isSinAsignar = !p.repartidorId;
          const isAsignadoAMi = p.repartidorId === repId;

          const estadoLower = p.estadoNombre?.toLowerCase() || '';
          const isTerminado = estadoLower.includes('entregado') || estadoLower.includes('cancelado');

          if (isTerminado) return false;

          // Si el pedido no está asignado, solo se permite asignarse si el restaurante lo está PREPARANDO
          if (isSinAsignar && !estadoLower.includes('preparando')) {
            return false;
          }

          return isSinAsignar || isAsignadoAMi;
        });

        this.pedidosDisponibles.set(filtrados);
        this.loadingPedidos.set(false);
      },
      error: () => {
        this.loadingPedidos.set(false);
      }
    });
  }

  asignarRepartidor(pedido: PedidoOutputDto) {
    const repId = this.repartidor()?.id;
    if (!repId || this.asignandoPedidoId()) return;

    this.asignandoPedidoId.set(pedido.id);
    this.pedidoService.obtenerPorId(pedido.id).subscribe({
      next: (pedidoCompleto) => {
        const payload = {
          clienteId: pedidoCompleto.clienteId,
          empresaId: pedidoCompleto.empresaId,
          idRepartidor: repId,
          repartidorId: repId,
          detalles: pedidoCompleto.detalles.map(d => ({
            pedidoId: pedidoCompleto.id,
            productoId: d.productoId,
            cantidad: d.cantidad,
            precioUnitario: d.precioUnitario
          }))
        };

        this.pedidoService.actualizar(pedido.id, payload).subscribe({
          next: () => {
            // Tras asignarse exitosamente, transicionar automáticamente el estado a "EN CAMINO"
            const estadoEnCamino = this.estadosList().find(e => e.nombre?.toLowerCase().includes('camino'));
            if (estadoEnCamino) {
              this.pedidoService.actualizarEstado(pedido.id, estadoEnCamino.id).subscribe({
                next: () => {
                  this.asignandoPedidoId.set(null);
                  this.cargarPedidosRepartidor();
                },
                error: () => {
                  this.asignandoPedidoId.set(null);
                  this.cargarPedidosRepartidor();
                }
              });
            } else {
              this.asignandoPedidoId.set(null);
              this.cargarPedidosRepartidor();
            }
          },
          error: () => {
            this.asignandoPedidoId.set(null);
          }
        });
      },
      error: () => {
        this.asignandoPedidoId.set(null);
      }
    });
  }

  marcarComoEntregado(pedidoId: number) {
    const estadoEntregado = this.estadosList().find(e => e.nombre?.toLowerCase().includes('entregado'));
    if (!estadoEntregado) {
      alert('No se encontró el estado "Entregado" en la configuración del sistema.');
      return;
    }

    this.actualizandoEstadoPedidoId.set(pedidoId);
    this.pedidoService.actualizarEstado(pedidoId, estadoEntregado.id).subscribe({
      next: () => {
        this.actualizandoEstadoPedidoId.set(null);
        this.successModalMessage.set('Usted ha entregado el pedido con exito');
        this.isSuccessModalOpen.set(true);
        this.cargarPedidosRepartidor();
      },
      error: () => {
        this.actualizandoEstadoPedidoId.set(null);
        this.cargarPedidosRepartidor();
      }
    });
  }

  toggleDisponibilidad(event: any) {
    const rep = this.repartidor();
    if (!rep) return;
    const isDisponible = event.detail ? event.detail.checked : event.target.checked;
    this.repartidorService.actualizarDisponibilidad(rep.id, isDisponible).subscribe({
      next: (updated) => {
        this.repartidor.set(updated);
        if (updated.disponible) {
          this.cargarEstados();
          this.cargarPedidosRepartidor();
        } else {
          this.pedidosDisponibles.set([]);
        }
      },
      error: () => {
        // revert local state on error
        this.repartidor.set({ ...rep });
      }
    });
  }

  getEstadoColor(estado: string): string {
    const estadoLower = estado?.toLowerCase() || '';
    if (estadoLower.includes('pendiente')) return 'bg-yellow-100 text-yellow-800 border-yellow-200';
    if (estadoLower.includes('preparando')) return 'bg-blue-100 text-blue-800 border-blue-200';
    if (estadoLower.includes('camino')) return 'bg-purple-100 text-purple-800 border-purple-200';
    if (estadoLower.includes('entregado')) return 'bg-green-100 text-green-800 border-green-200';
    if (estadoLower.includes('cancelado')) return 'bg-red-100 text-red-800 border-red-200';
    return 'bg-gray-100 text-gray-800 border-gray-200';
  }

  onPerfilActualizado(data: any) {
    this.cliente.set(data);
  }

  abrirChatCliente(pedido: PedidoOutputDto) {
    if (pedido.clienteId) {
      this.chatPedidoId.set(pedido.id);
      this.chatReceptorId.set(pedido.clienteId);
      this.chatReceptorNombre.set(pedido.nombreCliente || 'Cliente');
      this.isChatOpen.set(true);
    }
  }

  enviarUbicacionLive(pedido: PedidoOutputDto) {
    // Placeholder para inicializar la transmisión por Sockets de coordenadas GPS
    console.log('Emitiendo ubicación en vivo por socket para el pedido:', pedido.id);
    alert('Transmitiendo tu ubicación en tiempo real al cliente mediante WebSockets.');
  }

  volver() {
    this.router.navigate(['/restaurantes']);
  }
}



