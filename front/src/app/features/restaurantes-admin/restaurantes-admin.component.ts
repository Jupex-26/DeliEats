import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
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
import { EmpresaOutputDto, EmpresaInputDto, ProductoOutputDto, ProductoInputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

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
    IonItem,
    CurrencyPipe,
  ],
  templateUrl: './restaurantes-admin.component.html',
  styleUrls: ['./restaurantes-admin.component.scss'],
})
export class RestaurantesAdminComponent implements OnInit {
  private empresaService = inject(EmpresaService);
  private productoService = inject(ProductoService);

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

  // --- Estado de Productos ---
  isProductsModalOpen = signal(false);
  isProductFormModalOpen = signal(false);
  isProductConfirmModalOpen = signal(false);

  selectedEmpresaForProducts = signal<EmpresaOutputDto | null>(null);
  productos = signal<ProductoOutputDto[]>([]);
  editingProducto = signal<ProductoOutputDto | null>(null);
  productoIdParaEliminar = signal<number | null>(null);

  productoForm: ProductoInputDto = this.getEmptyProductoForm(0);

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
    this.cargarEmpresas();
  }

  // ==========================================
  // GESTIÓN DE RESTAURANTES (EMPRESAS)
  // ==========================================

  cargarEmpresas() {
    // Nota: El backend podría no soportar búsqueda por término en empresas, pero lo pasamos por si acaso
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
    this.empresaForm = { ...empresa, rolId: 3 }; // Asumiendo rolId 3 para Empresa
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
      tipoCocina: '',
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
        // Actualizamos también la lista general para reflejar cambios
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
    const editId = this.editingProducto()?.id;
    const request = editId
      ? this.productoService.actualizar(editId, this.productoForm)
      : this.productoService.crear(this.productoForm);

    request.subscribe(() => {
      this.isProductFormModalOpen.set(false);
      this.recargarProductosDeEmpresa();
    });
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
