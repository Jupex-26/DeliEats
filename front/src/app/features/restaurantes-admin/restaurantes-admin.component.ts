import { Component, inject, OnInit, OnDestroy, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { timer, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonButtons,
  IonButton,
  IonIcon,
  IonContent,
  IonModal,
  IonInput,
  IonItem,
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
  mailOutline,
  callOutline,
  fastFoodOutline,
  restaurantOutline,
  locationOutline
} from 'ionicons/icons';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { ProductoService } from '../../services/producto/producto-service';
import { TipoCocinaService } from '../../services/tipococina/tipococina-service';
import { EmpresaOutputDto, EmpresaInputDto, ProductoOutputDto, ProductoInputDto, TipoCocinaOutputDto, CustomError } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';

import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';

@Component({
  selector: 'app-restaurantes-admin',
  standalone: true,
  imports: [
    FormsModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonButtons,
    IonButton,
    IonIcon,
    IonContent,
    IonModal,
    IonInput,
    ConfirmModalComponent,
    InfoModalComponent,
    IonItem,
    CurrencyPipe,
  ],
  templateUrl: './restaurantes-admin.component.html',
  styleUrls: ['./restaurantes-admin.component.scss'],
})
export class RestaurantesAdminComponent implements OnInit {
  private empresaService = inject(EmpresaService);
  private productoService = inject(ProductoService);
  private tipoCocinaService = inject(TipoCocinaService);
  private pollSubscription?: Subscription;

  // --- Estado de Restaurantes ---
  empresas = signal<EmpresaOutputDto[]>([]);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  terminoBusqueda = signal('');

  isModalOpen = signal(false);
  isConfirmModalOpen = signal(false);
  isViewModalOpen = signal(false);

  editingEmpresa = signal<EmpresaOutputDto | null>(null);
  viewingEmpresa = signal<EmpresaOutputDto | null>(null);
  empresaIdParaEliminar = signal<number | null>(null);

  empresaForm: EmpresaInputDto = this.getEmptyEmpresaForm();
  tiposCocina = signal<TipoCocinaOutputDto[]>([]);

  // --- Estado de Productos ---
  isProductsModalOpen = signal(false);
  isProductFormModalOpen = signal(false);
  isProductConfirmModalOpen = signal(false);

  selectedEmpresaForProducts = signal<EmpresaOutputDto | null>(null);
  productos = signal<ProductoOutputDto[]>([]);
  editingProducto = signal<ProductoOutputDto | null>(null);
  productoIdParaEliminar = signal<number | null>(null);

  productoForm: ProductoInputDto = this.getEmptyProductoForm(0);

  // Modal Info
  isInfoModalOpen = signal(false);
  modalTitle = signal('');
  modalMessage = signal('');
  modalType = signal<'success' | 'error' | 'info'>('info');
  modalErrorData = signal<CustomError | null>(null);

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
      mailOutline,
      callOutline,
      fastFoodOutline,
      restaurantOutline,
      locationOutline
    });

    this.debouncer.pipe(debounceTime(400), distinctUntilChanged()).subscribe((valor) => {
      this.terminoBusqueda.set(valor);
      this.currentPage.set(0);
      this.cargarEmpresas();
    });
  }

  ngOnInit() {
    this.cargarTiposCocina();
    // Polling cada 15 segundos para administración en "tiempo real"
    this.pollSubscription = timer(0, 15000).pipe(
      switchMap(() => this.empresaService.listar(this.currentPage(), this.pageSize()))
    ).subscribe({
      next: (response) => {
        this.empresas.set(response.content);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);
      },
      error: (err) => console.error('Error polling empresas', err)
    });
  }

  ngOnDestroy() {
    this.pollSubscription?.unsubscribe();
  }

  // ==========================================
  // GESTIÓN DE RESTAURANTES (EMPRESAS)
  // ==========================================

  cargarTiposCocina() {
    this.tipoCocinaService.listar(0, 100).subscribe({
      next: (res) => this.tiposCocina.set(res.content),
      error: (err) => console.error('Error al cargar tipos de cocina', err)
    });
  }

  cargarEmpresas() {
    this.empresaService
      .listar(this.currentPage(), this.pageSize())
      .subscribe({
        next: (response) => {
          this.empresas.set(response.content);
          this.totalPages.set(response.totalPages);
          this.totalElements.set(response.totalElements);
        },
      });
  }

  onSearch(event: any) {
    this.debouncer.next(event.target.value);
  }

  cambiarPagina(delta: number) {
    this.currentPage.update((p) => p + delta);
    this.cargarEmpresas();
  }

  abrirModalNuevo() {
    this.editingEmpresa.set(null);
    this.empresaForm = this.getEmptyEmpresaForm();
    this.isModalOpen.set(true);
  }

  abrirModalEditar(empresa: EmpresaOutputDto) {
    this.editingEmpresa.set(empresa);
    this.empresaForm = {
      ...empresa,
      rolId: 3,
      tipoCocinaId: empresa.tipoCocina.id
    };
    this.isModalOpen.set(true);
  }

  abrirModalVer(empresa: EmpresaOutputDto) {
    this.viewingEmpresa.set(empresa);
    this.isViewModalOpen.set(true);
  }

  confirmarEliminar(id: number) {
    this.empresaIdParaEliminar.set(id);
    this.isConfirmModalOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.empresaIdParaEliminar();
    if (id) {
      this.empresaService.eliminar(id).subscribe(() => {
        this.isConfirmModalOpen.set(false);
        this.cargarEmpresas();
      });
    }
  }

  guardarEmpresa() {
    const editId = this.editingEmpresa()?.id;
    const request = editId
      ? this.empresaService.actualizar(editId, this.empresaForm)
      : this.empresaService.crear(this.empresaForm);

    request.subscribe(() => {
      this.isModalOpen.set(false);
      this.cargarEmpresas();
    });
  }

  private getEmptyEmpresaForm(): EmpresaInputDto {
    return {
      nombre: '',
      email: '',
      telefono: undefined,
      direccion: '',
      foto: '',
      rolId: 3, // Rol Empresa
      descripcion: '',
      correoContacto: '',
      telefonoContacto: '',
      tipoCocinaId: 0,
    };
  }

  // ==========================================
  // GESTIÓN DE PRODUCTOS
  // ==========================================

  abrirModalProductos(empresa: EmpresaOutputDto) {
    this.selectedEmpresaForProducts.set(empresa);
    this.productos.set(empresa.productos || []);
    this.isProductsModalOpen.set(true);
  }

  recargarProductosDeEmpresa() {
    const empresaId = this.selectedEmpresaForProducts()?.id;
    if (empresaId) {
      this.empresaService.obtenerPorId(empresaId).subscribe((empresaActualizada) => {
        this.productos.set(empresaActualizada.productos || []);
        this.cargarEmpresas();
      });
    }
  }

  abrirModalNuevoProducto() {
    this.editingProducto.set(null);
    this.productoForm = this.getEmptyProductoForm(this.selectedEmpresaForProducts()!.id!);
    this.isProductFormModalOpen.set(true);
  }

  abrirModalEditarProducto(producto: ProductoOutputDto) {
    this.editingProducto.set(producto);
    this.productoForm = { ...producto };
    this.isProductFormModalOpen.set(true);
  }

  confirmarEliminarProducto(id: number) {
    this.productoIdParaEliminar.set(id);
    this.isProductConfirmModalOpen.set(true);
  }

  ejecutarEliminacionProducto() {
    const id = this.productoIdParaEliminar();
    if (id) {
      this.productoService.eliminar(id).subscribe(() => {
        this.isProductConfirmModalOpen.set(false);
        this.recargarProductosDeEmpresa();
      });
    }
  }

  guardarProducto() {
    if (this.productoForm.precio <= 0) {
      this.showError('Error de validación', { error: { message: 'El precio debe ser mayor que 0' } });
      return;
    }

    const editId = this.editingProducto()?.id;
    const request = editId
      ? this.productoService.actualizar(editId, this.productoForm)
      : this.productoService.crear(this.productoForm);

    request.subscribe({
      next: () => {
        this.isProductFormModalOpen.set(false);
        this.recargarProductosDeEmpresa();
      },
      error: (err) => {
        this.showError(editId ? 'Error al actualizar producto' : 'Error al crear producto', err);
      }
    });
  }

  private showError(title: string, err: any) {
    this.modalTitle.set(title);
    this.modalType.set('error');
    this.modalMessage.set(err?.error?.message ?? 'Ha ocurrido un error inesperado.');
    this.modalErrorData.set(err?.error || null);
    this.isInfoModalOpen.set(true);
  }

  onModalClosed() {
    this.isInfoModalOpen.set(false);
  }

  private getEmptyProductoForm(empresaId: number): ProductoInputDto {
    return {
      nombre: '',
      foto: '',
      descripcion: '',
      precio: 0,
      cantidad: 0,
      empresaId: empresaId,
    };
  }
}
