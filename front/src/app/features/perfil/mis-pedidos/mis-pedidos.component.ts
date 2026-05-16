import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  receiptOutline,
  downloadOutline,
  closeCircleOutline,
  chevronForwardOutline,
  refreshOutline,
  timeOutline,
  bicycleOutline,
  checkmarkCircleOutline
} from 'ionicons/icons';
import { PedidoService } from '../../../services/pedido/pedido-service';
import { AuthService } from '../../../services/auth/auth-service';
import { PedidoOutputDto } from '../../../types';
import { EuroPipe } from '../../../pipe/euro.pipe';
import { Subscription } from 'rxjs';

type EstadoPedido = 'PENDIENTE' | 'PREPARANDO' | 'EN CAMINO' | 'ENTREGADO' | 'CANCELADO';

@Component({
  selector: 'app-mis-pedidos',
  standalone: true,
  imports: [CommonModule, IonIcon, EuroPipe],
  templateUrl: './mis-pedidos.component.html',
  styleUrls: ['./mis-pedidos.component.scss']
})
export class MisPedidosComponent implements OnInit, OnDestroy {
  private pedidoService = inject(PedidoService);
  private authService = inject(AuthService);
  private router = inject(Router);

  pedidos = signal<PedidoOutputDto[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  descargando = signal<number | null>(null);
  private refreshSub?: Subscription;

  constructor() {
    addIcons({
      receiptOutline,
      downloadOutline,
      closeCircleOutline,
      chevronForwardOutline,
      refreshOutline,
      timeOutline,
      bicycleOutline,
      checkmarkCircleOutline
    });
  }

  ngOnInit() {
    this.cargarPedidos();
    this.refreshSub = this.pedidoService.refreshPedidos$.subscribe(() => {
      this.cargarPedidos();
    });
  }

  ngOnDestroy() {
    this.refreshSub?.unsubscribe();
  }

  cargarPedidos() {
    const clienteId = this.authService.currentUser()?.userOutputDto?.id;
    if (!clienteId) return;

    this.loading.set(true);
    this.error.set(null);

    this.pedidoService.listarPorCliente(clienteId).subscribe({
      next: (res) => {
        this.pedidos.set(res.content ?? res ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los pedidos.');
        this.loading.set(false);
      }
    });
  }

  verDetalle(pedido: PedidoOutputDto) {
    this.router.navigate(['/pedidos', pedido.id]);
  }

  descargarFactura(pedido: PedidoOutputDto, event: Event) {
    event.stopPropagation();
    if (this.descargando() === pedido.id) return;

    this.descargando.set(pedido.id);
    this.pedidoService.descargarFactura(pedido.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `factura-pedido-${pedido.id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.descargando.set(null);
      },
      error: () => this.descargando.set(null)
    });
  }

  getEstadoIcono(estado: string): string {
    const map: Record<string, string> = {
      'PENDIENTE': 'time-outline',
      'PREPARANDO': 'refresh-outline',
      'EN CAMINO': 'bicycle-outline',
      'ENTREGADO': 'checkmark-circle-outline',
      'CANCELADO': 'close-circle-outline'
    };
    return map[estado] ?? 'receipt-outline';
  }

  esClickable(estado: string): boolean {
    return estado !== 'ENTREGADO' && estado !== 'CANCELADO';
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-ES', {
      day: '2-digit', month: 'long', year: 'numeric'
    });
  }
}