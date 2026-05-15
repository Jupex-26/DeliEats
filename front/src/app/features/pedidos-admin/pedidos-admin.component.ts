import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, CurrencyPipe } from '@angular/common';
import {
  IonIcon,
  IonModal,
  IonInput,
  IonItem
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  addCircleOutline,
  createOutline,
  trashOutline,
  chevronBackOutline,
  chevronForwardOutline,
  searchOutline,
  eyeOutline,
  closeOutline,
  cartOutline,
  cashOutline,
  personOutline,
  bicycleOutline,
  locationOutline
} from 'ionicons/icons';
import { PedidoService } from '../../services/pedido/pedido-service';
import { ClienteService } from '../../services/cliente/cliente-service';
import { ProductoService } from '../../services/producto/producto-service';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { EstadoService } from '../../services/estado/estado-service';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { PedidoOutputDto, PedidoInputDto } from '../../types/pedido';
import { DetallePedidoInputDto } from '../../types/detalle-pedido';
import { ClienteOutputDto, ProductoOutputDto, EmpresaOutputDto, EstadoOutputDto, RepartidorOutputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged, forkJoin } from 'rxjs';

@Component({
  selector: 'app-pedidos-admin',
  standalone: true,
  imports: [
    FormsModule,
    IonIcon,
    IonModal,
    IonInput,
    ConfirmModalComponent,
    IonItem,
    DatePipe,
    CurrencyPipe
  ],
  templateUrl: './pedidos-admin.component.html',
  styleUrls: ['./pedidos-admin.component.scss'],
})
export class PedidosAdminComponent implements OnInit {
  private pedidoService = inject(PedidoService);
  private clienteService = inject(ClienteService);
  private productoService = inject(ProductoService);
  private empresaService = inject(EmpresaService);
  private estadoService = inject(EstadoService);
  private repartidorService = inject(RepartidorService);

  pedidos = signal<PedidoOutputDto[]>([]);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  terminoBusqueda = signal('');

  isModalOpen = signal(false);
  isConfirmModalOpen = signal(false);
  isViewModalOpen = signal(false);

  editingPedido = signal<PedidoOutputDto | null>(null);
  viewingPedido = signal<PedidoOutputDto | null>(null);
  pedidoIdParaEliminar = signal<number | null>(null);

  clientesList = signal<ClienteOutputDto[]>([]);
  productosList = signal<ProductoOutputDto[]>([]);
  empresasList = signal<EmpresaOutputDto[]>([]);
  estadosList = signal<EstadoOutputDto[]>([]);
  repartidoresList = signal<RepartidorOutputDto[]>([]);

  tempEmpresaId: number | null = null;
  tempProductoId: number | null = null;
  tempCantidad: number = 1;

  pedidoForm: PedidoInputDto = this.getEmptyPedidoForm();

  private debouncer = new Subject<string>();

  constructor() {
    addIcons({
      addCircleOutline,
      createOutline,
      trashOutline,
      chevronBackOutline,
      chevronForwardOutline,
      searchOutline,
      eyeOutline,
      closeOutline,
      cartOutline,
      cashOutline,
      personOutline,
      bicycleOutline,
      locationOutline
    });

    this.debouncer.pipe(debounceTime(400), distinctUntilChanged()).subscribe((valor) => {
      this.terminoBusqueda.set(valor);
      this.currentPage.set(0);
      this.cargarPedidos();
    });
  }

  ngOnInit() {
    this.cargarPedidos();
    this.cargarListadosDesplegables();
  }

  cargarListadosDesplegables() {
    this.refrescarClientes();
    this.refrescarProductos();
    this.refrescarEmpresas();
    this.refrescarEstados();
    this.refrescarRepartidores();
  }

  refrescarClientes() {
    this.clienteService.listar(0, 1000).subscribe({
      next: (res) => this.clientesList.set(res.content)
    });
  }

  refrescarProductos() {
    this.productoService.listar(0, 1000).subscribe({
      next: (res) => this.productosList.set(res.content)
    });
  }

  refrescarEmpresas() {
    this.empresaService.listar(0, 1000).subscribe({
      next: (res) => this.empresasList.set(res.content)
    });
  }

  refrescarEstados() {
    this.estadoService.listar(0, 1000).subscribe({
      next: (res) => this.estadosList.set(res.content)
    });
  }

  refrescarRepartidores() {
    this.repartidorService.obtenerDisponibles(0, 1000).subscribe({
      next: (res) => this.repartidoresList.set(res.content)
    });
  }

  cargarPedidos() {
    if (this.terminoBusqueda() && !isNaN(Number(this.terminoBusqueda()))) {
      
      this.pedidoService
        .listarPorCliente(Number(this.terminoBusqueda()), this.currentPage(), this.pageSize())
        .subscribe({
          next: (response) => {
            this.pedidos.set(response.content);
            this.totalPages.set(response.totalPages);
            this.totalElements.set(response.totalElements);
          },
        });
    } else {
      
      this.pedidoService
        .listar(this.currentPage(), this.pageSize())
        .subscribe({
          next: (response) => {
            this.pedidos.set(response.content);
            this.totalPages.set(response.totalPages);
            this.totalElements.set(response.totalElements);
          },
        });
    }
  }

  onSearch(event: any) {
    this.debouncer.next(event.target.value);
  }

  cambiarPagina(delta: number) {
    this.currentPage.update((p) => p + delta);
    this.cargarPedidos();
  }

  abrirModalNuevo() {
    this.editingPedido.set(null);
    this.pedidoForm = this.getEmptyPedidoForm();
    this.nuevoEstadoId = null;
    this.tempEmpresaId = null;
    this.tempProductoId = null;
    this.tempCantidad = 1;
    this.isModalOpen.set(true);
  }

  nuevoEstadoId: number | null = null;

  getProductosFiltrados(): ProductoOutputDto[] {
    if (!this.tempEmpresaId) return [];
    return this.productosList().filter(p => p.empresaId == this.tempEmpresaId);
  }

  onEmpresaChange() {
    this.tempProductoId = null; 
  }

  agregarDetalle() {
    if (!this.tempProductoId || this.tempCantidad < 1 || !this.tempEmpresaId) return;

    const productoEncontrado = this.productosList().find(p => p.id == this.tempProductoId);
    if (!productoEncontrado) return;

    if (this.pedidoForm.detalles.length === 0) {
      this.pedidoForm.empresaId = this.tempEmpresaId;
    }

    const nuevoDetalle: DetallePedidoInputDto = {
      pedidoId: 0, 
      productoId: productoEncontrado.id,
      cantidad: this.tempCantidad,
      precioUnitario: productoEncontrado.precio
    };

    const detallesActuales = this.pedidoForm.detalles;
    const existe = detallesActuales.findIndex(d => d.productoId === productoEncontrado.id);
    if (existe !== -1) {
      detallesActuales[existe].cantidad += this.tempCantidad;
    } else {
      detallesActuales.push(nuevoDetalle);
    }

    this.tempProductoId = null;
    this.tempCantidad = 1;
  }

  eliminarDetalle(index: number) {
    this.pedidoForm.detalles.splice(index, 1);
  }

  getNombreProducto(productoId: number): string {
    return this.productosList().find(p => p.id === productoId)?.nombre || 'Producto desconocido';
  }

  abrirModalEditar(pedido: PedidoOutputDto) {
    this.pedidoService.obtenerPorId(pedido.id).subscribe({
      next: (pedidoCompleto) => {
        this.editingPedido.set(pedidoCompleto);
        
        this.pedidoForm = {
          clienteId: pedidoCompleto.clienteId,
          empresaId: pedidoCompleto.empresaId,
          idRepartidor: pedidoCompleto.repartidorId,
          detalles: pedidoCompleto.detalles.map(d => ({
            pedidoId: pedidoCompleto.id,
            productoId: d.productoId,
            cantidad: d.cantidad,
            precioUnitario: d.precioUnitario
          }))
        };

        const estadoActual = this.estadosList().find(e => e.nombre === pedidoCompleto.estadoNombre);
        this.nuevoEstadoId = estadoActual ? estadoActual.id : null;

        this.isModalOpen.set(true);
      }
    });
  }

  abrirModalVer(pedido: PedidoOutputDto) {
    this.pedidoService.obtenerPorId(pedido.id).subscribe({
      next: (pedidoCompleto) => {
        this.viewingPedido.set(pedidoCompleto);
        this.isViewModalOpen.set(true);
      }
    });
  }

  confirmarEliminar(id: number) {
    this.pedidoIdParaEliminar.set(id);
    this.isConfirmModalOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.pedidoIdParaEliminar();
    if (id) {
      this.pedidoService.eliminar(id).subscribe(() => {
        this.isConfirmModalOpen.set(false);
        this.cargarPedidos();
      });
    }
  }

  guardarPedido() {
    const editId = this.editingPedido()?.id;
    if (editId) {
      const peticiones = [];
      
      peticiones.push(this.pedidoService.actualizar(editId, this.pedidoForm));
      
      if (this.nuevoEstadoId) {
        peticiones.push(this.pedidoService.actualizarEstado(editId, this.nuevoEstadoId));
      }

      forkJoin(peticiones).subscribe(() => {
        this.isModalOpen.set(false);
        this.cargarPedidos();
      });
    } else {
      
      this.pedidoService.crear(this.pedidoForm).subscribe(() => {
        this.isModalOpen.set(false);
        this.cargarPedidos();
      });
    }
  }

  private getEmptyPedidoForm(): PedidoInputDto {
    return {
      clienteId: 0,
      empresaId: 0,
      detalles: []
    };
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
}