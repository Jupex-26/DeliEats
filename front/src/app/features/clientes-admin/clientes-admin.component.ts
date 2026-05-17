import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
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
  IonSpinner,
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
  eyeOffOutline,
  closeOutline,
  mailOutline,
  callOutline,
  calendarOutline,
  locationOutline,
  refreshOutline
} from 'ionicons/icons';
import { ClienteService } from '../../services/cliente/cliente-service';
import { ClienteOutputDto, ClienteInputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged, finalize } from 'rxjs';
import { DatePipe } from '@angular/common';
import { environment } from '../../../environments/environment';
import { Validador } from '../../validadores/validador';
import { GeocodingService } from '../../services/geocoding/geocoding-service';

@Component({
  selector: 'app-clientes-admin',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
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
    IonSpinner,
    DatePipe,
  ],
  templateUrl: './clientes-admin.component.html',
  styleUrls: ['./clientes-admin.component.scss'],
})
export class ClientesAdminComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private fb = inject(FormBuilder);
  private geocodingService = inject(GeocodingService);

  clientes = signal<ClienteOutputDto[]>([]);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  terminoBusqueda = signal('');

  isModalOpen = signal(false);
  isConfirmModalOpen = signal(false);
  isViewModalOpen = signal(false);
  editingCliente = signal<ClienteOutputDto | null>(null);
  viewingCliente = signal<ClienteOutputDto | null>(null);
  clienteIdParaEliminar = signal<number | null>(null);
  protected environment = environment;
  isLoading = signal(false);
  showPassword = signal(false);
  validandoDireccion = signal(false);
  errorDireccion = signal(false);

  readonly fechaLimite16 = new Date(new Date().setFullYear(new Date().getFullYear() - 16));

  form: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    fechaNacimiento: ['', [Validators.required, Validador.before(this.fechaLimite16)]],
    rolId: [2],
  });

  refrescarTodo() {
    this.cargarClientes();
  }

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
      eyeOffOutline,
      closeOutline,
      mailOutline,
      callOutline,
      calendarOutline,
      locationOutline,
      refreshOutline
    });

    this.debouncer.pipe(debounceTime(400), distinctUntilChanged()).subscribe((valor) => {
      this.terminoBusqueda.set(valor);
      this.currentPage.set(0);
      this.cargarClientes();
    });
  }

  ngOnInit() {
    this.cargarClientes();
  }

  cargarClientes() {
    this.clienteService
      .listar(this.currentPage(), this.pageSize(), this.terminoBusqueda())
      .subscribe({
        next: (response) => {
          this.clientes.set(response.content);
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
    this.cargarClientes();
  }

  private updateFormMode() {
    const passwordControl = this.form.get('password');
    if (this.editingCliente()) {
      passwordControl?.clearValidators();
      passwordControl?.updateValueAndValidity();
    } else {
      passwordControl?.setValidators([Validators.required, Validador.isStrongPassword]);
      passwordControl?.updateValueAndValidity();
    }
  }

  abrirModalNuevo() {
    this.editingCliente.set(null);
    this.showPassword.set(false);
    this.errorDireccion.set(false);
    this.form.reset({
      nombre: '',
      email: '',
      password: '',
      telefono: '',
      direccion: '',
      fechaNacimiento: '',
      rolId: 2
    });
    this.updateFormMode();
    this.isModalOpen.set(true);
  }

  abrirModalEditar(cliente: ClienteOutputDto) {
    this.editingCliente.set(cliente);
    this.showPassword.set(false);
    this.errorDireccion.set(false);
    this.updateFormMode();
    this.form.patchValue({
      nombre: cliente.nombre,
      email: cliente.email,
      password: '',
      telefono: cliente.telefono?.toString() || '',
      direccion: cliente.direccion || '',
      fechaNacimiento: cliente.fechaNacimiento ? cliente.fechaNacimiento.split('T')[0] : '',
      rolId: 2
    });
    this.isModalOpen.set(true);
  }

  abrirModalVer(cliente: ClienteOutputDto) {
    this.viewingCliente.set(cliente);
    this.isViewModalOpen.set(true);
  }

  confirmarEliminar(id: number) {
    this.clienteIdParaEliminar.set(id);
    this.isConfirmModalOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.clienteIdParaEliminar();
    if (id) {
      this.clienteService.eliminar(id).subscribe(() => {
        this.isConfirmModalOpen.set(false);
        this.cargarClientes();
      });
    }
  }

  guardarCliente() {
    if (this.form.valid) {
      const value = this.form.value;
      const direccion = value.direccion!;

      this.validandoDireccion.set(true);
      this.errorDireccion.set(false);

      this.geocodingService.verificarDireccion(direccion).subscribe((isValid: boolean) => {
        this.validandoDireccion.set(false);
        if (!isValid) {
          this.errorDireccion.set(true);
          return;
        }

        const clienteData: ClienteInputDto = {
          nombre: value.nombre!,
          email: value.email!,
          password: value.password || '',
          telefono: value.telefono ? Number(value.telefono) : undefined,
          direccion: direccion,
          fechaNacimiento: value.fechaNacimiento ? new Date(value.fechaNacimiento).toISOString() : '',
          rolId: 2,
        };

        const editId = this.editingCliente()?.id;
        const request = editId
          ? this.clienteService.actualizar(editId, clienteData)
          : this.clienteService.crear(clienteData);

        this.isLoading.set(true);
        request.pipe(
          finalize(() => this.isLoading.set(false))
        ).subscribe({
          next: () => {
            this.isModalOpen.set(false);
            this.cargarClientes();
          },
          error: (err) => {
            console.error('Error al guardar cliente:', err);
          }
        });
      });
    }
  }
}