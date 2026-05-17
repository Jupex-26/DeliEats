import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  IonIcon,
  IonModal,
  IonInput,
  IonItem,
  IonToggle,
  IonButton,
  IonSpinner
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
  locationOutline,
  checkmarkCircleOutline,
  refreshOutline
} from 'ionicons/icons';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { UserService } from '../../services/user/user-service';
import { ClienteService } from '../../services/cliente/cliente-service';
import { GeocodingService } from '../../services/geocoding/geocoding-service';
import { Validador } from '../../validadores/validador';
import { RepartidorOutputDto, RepartidorInputDto, ClienteInputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged, finalize } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-repartidores-admin',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    IonIcon,
    IonModal,
    IonInput,
    ConfirmModalComponent,
    IonItem,
    IonToggle,
    IonButton,
    IonSpinner
  ],
  templateUrl: './repartidores-admin.component.html',
  styleUrls: ['./repartidores-admin.component.scss'],
})
export class RepartidoresAdminComponent implements OnInit {
  private fb = inject(FormBuilder);
  private repartidorService = inject(RepartidorService);
  private userService = inject(UserService);
  private clienteService = inject(ClienteService);
  private geocodingService = inject(GeocodingService);

  repartidores = signal<RepartidorOutputDto[]>([]);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  terminoBusqueda = signal('');
  solicitudesCount = signal(0);
  mostrarSolicitudes = signal(false);

  isModalOpen = signal(false);
  isConfirmModalOpen = signal(false);
  isViewModalOpen = signal(false);

  editingRepartidor = signal<RepartidorOutputDto | null>(null);
  viewingRepartidor = signal<RepartidorOutputDto | null>(null);
  repartidorIdParaEliminar = signal<number | null>(null);
  clienteIdParaEliminar = signal<number | null>(null);

  protected environment = environment;
  showPassword = signal(false);
  validandoDireccion = signal(false);
  errorDireccion = signal(false);
  isLoading = signal(false);

  readonly fechaLimite16 = new Date(new Date().setFullYear(new Date().getFullYear() - 16));

  form: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    fechaNacimiento: ['', [Validators.required, Validador.before(this.fechaLimite16)]],
    rolId: [4],
  });

  refrescarTodo() {
    this.cargarRepartidores();
    this.cargarSolicitudesCount();
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
      locationOutline,
      checkmarkCircleOutline,
      refreshOutline
    });

    this.debouncer.pipe(debounceTime(400), distinctUntilChanged()).subscribe((valor) => {
      this.terminoBusqueda.set(valor);
      this.currentPage.set(0);
      this.cargarRepartidores();
    });
  }

  ngOnInit() {
    this.cargarRepartidores();
    this.cargarSolicitudesCount();
  }

  cargarSolicitudesCount() {
    this.repartidorService.obtenerPorAprobado(false, 0, 1).subscribe({
      next: (res: any) => this.solicitudesCount.set(res.totalElements)
    });
  }

  cargarRepartidores() {
    const filtroAprobado = !this.mostrarSolicitudes();
    this.repartidorService
      .obtenerPorAprobado(filtroAprobado, this.currentPage(), this.pageSize(), this.terminoBusqueda())
      .subscribe({
        next: (response: any) => {
          this.repartidores.set(response.content);
          this.totalPages.set(response.totalPages);
          this.totalElements.set(response.totalElements);
        },
        error: (err: any) => console.error('Error al cargar repartidores:', err)
      });
  }

  setMostrarSolicitudes(valor: boolean) {
    this.mostrarSolicitudes.set(valor);
    this.currentPage.set(0);
    this.cargarRepartidores();
  }

  aprobarRepartidor(repartidor: RepartidorOutputDto) {
    this.userService.aprobarRepartidor(repartidor.clienteId, true).subscribe({
      next: () => {
        this.cargarRepartidores();
        this.cargarSolicitudesCount();
      }
    });
  }

  onSearch(event: any) {
    this.debouncer.next(event.target.value);
  }

  cambiarPagina(delta: number) {
    this.currentPage.update((p: number) => p + delta);
    this.cargarRepartidores();
  }

  abrirModalNuevo() {
    this.editingRepartidor.set(null);
    this.form.reset({ rolId: 4 });
    this.updateFormMode();
    this.isModalOpen.set(true);
  }

  private updateFormMode() {
    const passwordControl = this.form.get('password');
    if (this.editingRepartidor()) {
      passwordControl?.clearValidators();
      passwordControl?.updateValueAndValidity();
    } else {
      passwordControl?.setValidators([Validators.required, Validador.isStrongPassword]);
      passwordControl?.updateValueAndValidity();
    }
  }

  abrirModalEditar(repartidor: RepartidorOutputDto) {
    this.editingRepartidor.set(repartidor);
    this.updateFormMode();
    this.form.patchValue({
      nombre: repartidor.nombre || repartidor.cliente?.nombre || '',
      email: repartidor.email || repartidor.cliente?.email || '',
      telefono: repartidor.telefono || repartidor.cliente?.telefono || '',
      direccion: repartidor.direccion || repartidor.cliente?.direccion || '',
      fechaNacimiento: (repartidor.cliente?.fechaNacimiento || '').split('T')[0],
      rolId: 4
    });
    this.isModalOpen.set(true);
  }

  abrirModalVer(repartidor: RepartidorOutputDto) {
    this.viewingRepartidor.set(repartidor);
    this.isViewModalOpen.set(true);
  }

  confirmarEliminar(repartidor: RepartidorOutputDto) {
    this.repartidorIdParaEliminar.set(repartidor.id);
    this.clienteIdParaEliminar.set(repartidor.clienteId);
    this.isConfirmModalOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.clienteIdParaEliminar();
    if (id) {
      this.clienteService.eliminar(id).subscribe(() => {
        this.isConfirmModalOpen.set(false);
        this.cargarRepartidores();
      });
    }
  }

  guardarRepartidor() {
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
          rolId: 4,
        };

        const request = this.editingRepartidor()
          ? this.clienteService.actualizar(this.editingRepartidor()!.clienteId, clienteData)
          : this.clienteService.crear(clienteData);

        this.isLoading.set(true);
        request.pipe(
          finalize(() => this.isLoading.set(false))
        ).subscribe({
          next: () => {
            this.isModalOpen.set(false);
            this.cargarRepartidores();
          },
          error: (err: any) => {
            console.error('Error al guardar repartidor:', err);
          }
        });
      });
    }
  }

  toggleDisponibilidad(repartidor: RepartidorOutputDto, event: any) {
    event.stopPropagation();
    const isDisponible = event.detail.checked;
    this.repartidorService.actualizarDisponibilidad(repartidor.id, isDisponible).subscribe({
      next: () => {
        const actualizados = this.repartidores().map((r: RepartidorOutputDto) =>
          r.id === repartidor.id ? { ...r, disponible: isDisponible } : r
        );
        this.repartidores.set(actualizados);
      },
      error: () => {
        const prev = this.repartidores().map((r: RepartidorOutputDto) => r);
        this.repartidores.set(prev);
      }
    });
  }

  private getEmptyRepartidorForm(): ClienteInputDto {
    return {
      nombre: '',
      email: '',
      telefono: undefined,
      direccion: '',
      foto: '',
      rolId: 4, // ROLE_REPARTIDOR
      fechaNacimiento: '',
    };
  }
}