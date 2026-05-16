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
  cartOutline,
  refreshOutline
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

  pedidos = signal<PedidoOutputDto[]>([]);
  loading = signal(true);
  currentPage = signal(0);
  totalPages = signal(0);
  totalElem = signal(0);
  pageSize = 8;

  filtroActivo = signal<PedidoFiltro>('recientes');
  pedidoDetalle = signal<PedidoOutputDto | null>(null);

  estadosList = signal<EstadoOutputDto[]>([]);
  actualizandoEstadoId = signal<number | null>(null);

  mesSeleccionado = signal<number>(new Date().getMonth() + 1);
  anioSeleccionado = signal<number>(new Date().getFullYear());

  meses = [
    { id: 1, nombre: 'Enero' }, { id: 2, nombre: 'Febrero' }, { id: 3, nombre: 'Marzo' },
    { id: 4, nombre: 'Abril' }, { id: 5, nombre: 'Mayo' }, { id: 6, nombre: 'Junio' },
    { id: 7, nombre: 'Julio' }, { id: 8, nombre: 'Agosto' }, { id: 9, nombre: 'Septiembre' },
    { id: 10, nombre: 'Octubre' }, { id: 11, nombre: 'Noviembre' }, { id: 12, nombre: 'Diciembre' }
  ];

  anios: number[] = [];

  constructor() {
    addIcons({
      chevronBackOutline, chevronForwardOutline, receiptOutline,
      timeOutline, calendarOutline, cashOutline, eyeOutline,
      closeOutline, personOutline, bicycleOutline, locationOutline, cartOutline, refreshOutline
    });

    const year = new Date().getFullYear();
    for (let i = 0; i < 5; i++) {
      this.anios.push(year - i);
    }
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

  private getSortParam(): string {
    switch (this.filtroActivo()) {
      case 'recientes': return 'fechaCompra,desc';
      case 'mes': return 'fechaCompra,desc';
      case 'mayor-importe': return 'precio,desc';
      default: return 'fechaCompra,desc';
    }
  }

  cargarPedidos() {
    this.loading.set(true);
    const page = this.currentPage();
    const sort = this.getSortParam();

    const obs = this.filtroActivo() === 'mes'
      ? this.pedidoService.listarPorEmpresaYMes(this.empresaId, this.mesSeleccionado(), this.anioSeleccionado(), page, this.pageSize)
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

  recargar() {
    this.currentPage.set(0);
    this.cargarPedidos();
  }

  setFiltro(f: PedidoFiltro) {
    this.filtroActivo.set(f);
    this.currentPage.set(0);
    this.cargarPedidos();
  }

  cambiarMes(event: any) {
    this.mesSeleccionado.set(Number(event.target.value));
    this.currentPage.set(0);
    this.cargarPedidos();
  }

  cambiarAnio(event: any) {
    this.anioSeleccionado.set(Number(event.target.value));
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
    if (e.includes('pendiente')) return 'estado--pendiente';
    if (e.includes('preparando')) return 'estado--preparando';
    if (e.includes('camino')) return 'estado--camino';
    if (e.includes('entregado')) return 'estado--entregado';
    if (e.includes('cancelado')) return 'estado--cancelado';
    return '';
  }
}
