import { Component, Input, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonIcon, IonItem, IonInput, IonTextarea, IonModal } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  addCircleOutline,
  createOutline,
  trashOutline,
  closeOutline,
  imageOutline,
  chevronBackOutline,
  chevronForwardOutline,
  restaurantOutline,
  cubeOutline,
  pricetagOutline,
  searchOutline
} from 'ionicons/icons';
import { ProductoService } from '../../../services/producto/producto-service';
import { EuroPipe } from '../../../pipe/euro.pipe';
import { ConfirmModalComponent } from '../../../shared/confirm-modal/confirm-modal.component';
import { InfoModalComponent } from '../../../shared/info-modal/info-modal.component';
import { CustomError, EmpresaOutputDto, ProductoInputDto, ProductoOutputDto } from '../../../types';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-empresa-productos',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    IonIcon,
    IonItem,
    IonInput,
    IonTextarea,
    IonModal,
    EuroPipe,
    ConfirmModalComponent,
    InfoModalComponent
  ],
  templateUrl: './empresa-productos.component.html',
  styleUrls: ['./empresa-productos.component.scss']
})
export class EmpresaProductosComponent implements OnInit {
  @Input({ required: true }) empresa!: EmpresaOutputDto;

  protected environment = environment;
  private productoService = inject(ProductoService);
  private fb = inject(FormBuilder);

  productos = signal<ProductoOutputDto[]>([]);
  loading = signal(true);
  currentPage = signal(0);
  totalPages = signal(0);
  totalElem = signal(0);
  pageSize = 8;

  isModalOpen = signal(false);
  isConfirmOpen = signal(false);
  editingProducto = signal<ProductoOutputDto | null>(null);
  deletingId = signal<number | null>(null);
  guardando = signal(false);
  fotoFile = signal<File | null>(null);
  fotoPreview = signal<string | null>(null);

  // Modal Info
  isInfoModalOpen = signal(false);
  modalTitle = signal('');
  modalMessage = signal('');
  modalType = signal<'success' | 'error' | 'info'>('info');
  modalErrorData = signal<CustomError | null>(null);

  form = this.fb.group({
    nombre: ['', [Validators.required]],
    descripcion: ['', [Validators.required]],
    precio: [0, [Validators.required, Validators.min(0.01)]],
    cantidad: [1, [Validators.required, Validators.min(0)]]
  });

  constructor() {
    addIcons({
      addCircleOutline, createOutline, trashOutline, closeOutline,
      imageOutline, chevronBackOutline, chevronForwardOutline,
      restaurantOutline, cubeOutline, pricetagOutline, searchOutline
    });
  }

  ngOnInit() { this.cargarProductos(); }

  cargarProductos() {
    this.loading.set(true);
    this.productoService.listarPorEmpresa(this.empresa.id!, this.currentPage(), this.pageSize).subscribe({
      next: (res) => {
        this.productos.set(res.content);
        this.totalPages.set(res.totalPages);
        this.totalElem.set(res.totalElements);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  cambiarPagina(delta: number) {
    this.currentPage.update(p => p + delta);
    this.cargarProductos();
  }

  abrirModalNuevo() {
    this.editingProducto.set(null);
    this.fotoFile.set(null);
    this.fotoPreview.set(null);
    this.form.reset({ nombre: '', descripcion: '', precio: 0, cantidad: 1 });
    this.isModalOpen.set(true);
  }

  abrirModalEditar(p: ProductoOutputDto) {
    this.editingProducto.set(p);
    this.fotoFile.set(null);
    this.fotoPreview.set(p.foto ? environment.storageUrl + '/' + p.foto : null);
    this.form.patchValue({
      nombre: p.nombre,
      descripcion: p.descripcion,
      precio: p.precio,
      cantidad: p.cantidad
    });
    this.isModalOpen.set(true);
  }

  onFotoSeleccionada(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    this.fotoFile.set(file);
    const reader = new FileReader();
    reader.onload = (e) => this.fotoPreview.set(e.target?.result as string);
    reader.readAsDataURL(file);
  }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload: ProductoInputDto = {
      nombre: v.nombre!,
      descripcion: v.descripcion!,
      precio: v.precio ?? 0,
      cantidad: v.cantidad!,
      empresaId: this.empresa.id!
    };

    if (payload.precio <= 0) {
      this.showError('Error de validación', { error: { message: 'El precio debe ser mayor que 0' } });
      this.guardando.set(false);
      return;
    }

    const editing = this.editingProducto();
    const op$ = editing
      ? this.productoService.actualizar(editing.id, payload, this.fotoFile())
      : this.productoService.crear(payload, this.fotoFile());

    op$.subscribe({
      next: () => {
        this.guardando.set(false);
        this.isModalOpen.set(false);
        this.cargarProductos();
      },
      error: (err) => {
        this.guardando.set(false);
        this.showError(editing ? 'Error al actualizar' : 'Error al crear', err);
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

  confirmarEliminar(id: number) {
    this.deletingId.set(id);
    this.isConfirmOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.deletingId();
    if (!id) return;
    this.productoService.eliminar(id).subscribe({
      next: () => {
        this.isConfirmOpen.set(false);
        this.deletingId.set(null);
        this.cargarProductos();
      }
    });
  }
}
