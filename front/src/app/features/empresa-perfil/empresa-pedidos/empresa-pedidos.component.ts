import { Component, Input, OnInit, inject, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  chevronBackOutline,
  chevronForwardOutline,
  receiptOutline,
  timeOutline,
  calendarOutline,
  cashOutline,
  eyeOutline,
  closeOutline,
  personOutline,
  bicycleOutline,
  locationOutline,
  cartOutline
} from 'ionicons/icons';
import { PedidoService } from '../../../services/pedido/pedido-service';
import { EstadoService } from '../../../services/estado/estado-service';
import { EuroPipe } from '../../../pipe/euro.pipe';
import { PedidoOutputDto, EstadoOutputDto } from '../../../types';

export type PedidoFiltro = 'recientes' | 'mes' | 'mayor-importe';

@Component({
  selector: 'app-empresa-pedidos',
  standalone: true,
  imports: [CommonModule, IonIcon, EuroPipe, DatePipe],
  templateUrl: './empresa-pedidos.component.html',
  styleUrls: ['./empresa-pedidos.component.scss']
})
export class EmpresaPedidosComponent implements OnInit {
  @Input({ required: true }) empresaId!: number;

  private pedidoService = inject(PedidoService);
  private estadoService = inject(EstadoService);

  pedidos     = signal<PedidoOutputDto[]>([]);
  loading     = signal(true);
  currentPage = signal(0);
  totalPages  = signal(0);
  totalElem   = signal(0);
  pageSize    = 8;

  filtroActivo = signal<PedidoFiltro>('recientes');
  pedidoDetalle = signal<PedidoOutputDto | null>(null);

  // --- Estados de Pedidos ---
  estadosList = signal<EstadoOutputDto[]>([]);
  actualizandoEstadoId = signal<number | null>(null);

  constructor() {
    addIcons({
      chevronBackOutline, chevronForwardOutline, receiptOutline,
      timeOutline, calendarOutline, cashOutline, eyeOutline,
      closeOutline, personOutline, bicycleOutline, locationOutline, cartOutline
    });
  }

  ngOnInit() {
    this.cargarPedidos();
    this.cargarEstados();
  }

  cargarEstados() {
    this.estadoService.listar(0, 100).subscribe({
      next: (res) => this.estadosList.set(res.content || [])
    });
  }

  // Mapea el filtro visual al parámetro `sort` de Pageable
  private getSortParam(): string {
    switch (this.filtroActivo()) {
      case 'recientes':     return 'fechaCompra,desc';
      case 'mes':           return 'fechaCompra,desc';   // filtrado por mes → endpoint específico
      case 'mayor-importe': return 'precioTotal,desc';
      default:              return 'fechaCompra,desc';
    }
  }

  cargarPedidos() {
    this.loading.set(true);
    const page = this.currentPage();
    const sort = this.getSortParam();

    const obs = this.filtroActivo() === 'mes'
      ? this.pedidoService.listarPorEmpresaMesActual(this.empresaId, page, this.pageSize)
      : this.pedidoService.listarPorEmpresa(this.empresaId, page, this.pageSize, sort);

    obs.subscribe({
      next: (res) => {
        this.pedidos.set(res.content);
        this.totalPages.set(res.totalPages);
        this.totalElem.set(res.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  setFiltro(f: PedidoFiltro) {
    this.filtroActivo.set(f);
    this.currentPage.set(0);
    this.cargarPedidos();
  }

  cambiarPagina(delta: number) {
    this.currentPage.update(p => p + delta);
    this.cargarPedidos();
  }

  abrirDetalle(pedido: PedidoOutputDto) {
    this.pedidoService.obtenerPorId(pedido.id).subscribe({
      next: (p) => this.pedidoDetalle.set(p)
    });
  }

  cerrarDetalle() { this.pedidoDetalle.set(null); }

  actualizarEstado(pedidoId: number, event: any) {
    const nuevoEstadoId = Number(event.target.value);
    if (!nuevoEstadoId || isNaN(nuevoEstadoId)) return;

    this.actualizandoEstadoId.set(pedidoId);
    this.pedidoService.actualizarEstado(pedidoId, nuevoEstadoId).subscribe({
      next: () => {
        this.actualizandoEstadoId.set(null);
        this.cargarPedidos();
      },
      error: () => {
        this.actualizandoEstadoId.set(null);
        this.cargarPedidos();
      }
    });
  }

  isPedidoFinalizado(estadoNombre: string): boolean {
    const e = estadoNombre?.toLowerCase() ?? '';
    return e.includes('entregado') || e.includes('cancelado');
  }

  getEstadoClass(estado: string): string {
    const e = estado?.toLowerCase() ?? '';
    if (e.includes('pendiente'))  return 'estado--pendiente';
    if (e.includes('preparando')) return 'estado--preparando';
    if (e.includes('camino'))     return 'estado--camino';
    if (e.includes('entregado'))  return 'estado--entregado';
    if (e.includes('cancelado'))  return 'estado--cancelado';
    return '';
  }
}

