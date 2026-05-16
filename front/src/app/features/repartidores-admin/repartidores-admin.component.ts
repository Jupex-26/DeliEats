import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  IonIcon,
  IonModal,
  IonInput,
  IonItem,
  IonToggle
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
  locationOutline,
  checkmarkCircleOutline
} from 'ionicons/icons';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { UserService } from '../../services/user/user-service';
import { RepartidorOutputDto, RepartidorInputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-repartidores-admin',
  standalone: true,
  imports: [
    FormsModule,
    IonIcon,
    IonModal,
    IonInput,
    ConfirmModalComponent,
    IonItem,
    IonToggle
  ],
  templateUrl: './repartidores-admin.component.html',
  styleUrls: ['./repartidores-admin.component.scss'],
})
export class RepartidoresAdminComponent implements OnInit {
  private repartidorService = inject(RepartidorService);
  private userService = inject(UserService);

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

  protected environment = environment;
  repartidorForm: RepartidorInputDto = this.getEmptyRepartidorForm();

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
      locationOutline,
      checkmarkCircleOutline
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
      next: (res) => this.solicitudesCount.set(res.totalElements)
    });
  }

  cargarRepartidores() {
    
    const filtroAprobado = !this.mostrarSolicitudes();

    this.repartidorService
      .obtenerPorAprobado(filtroAprobado, this.currentPage(), this.pageSize())
      .subscribe({
        next: (response) => {
          this.repartidores.set(response.content);
          this.totalPages.set(response.totalPages);
          this.totalElements.set(response.totalElements);
        },
        error: (err) => console.error('Error al cargar repartidores:', err)
      });
  }

  setMostrarSolicitudes(valor: boolean) {
    this.mostrarSolicitudes.set(valor);
    this.currentPage.set(0);
    this.cargarRepartidores();
  }

  aprobarRepartidor(id: number) {
    this.userService.aprobarRepartidor(id, true).subscribe({
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
    this.currentPage.update((p) => p + delta);
    this.cargarRepartidores();
  }

  abrirModalNuevo() {
    this.editingRepartidor.set(null);
    this.repartidorForm = this.getEmptyRepartidorForm();
    this.isModalOpen.set(true);
  }

  abrirModalEditar(repartidor: RepartidorOutputDto) {
    this.editingRepartidor.set(repartidor);
    this.repartidorForm = { 
      clienteId: repartidor.clienteId,
      disponible: repartidor.disponible
    }; 
    this.isModalOpen.set(true);
  }

  abrirModalVer(repartidor: RepartidorOutputDto) {
    this.viewingRepartidor.set(repartidor);
    this.isViewModalOpen.set(true);
  }

  confirmarEliminar(id: number) {
    this.repartidorIdParaEliminar.set(id);
    this.isConfirmModalOpen.set(true);
  }

  ejecutarEliminacion() {
    const id = this.repartidorIdParaEliminar();
    if (id) {
      this.repartidorService.eliminar(id).subscribe(() => {
        this.isConfirmModalOpen.set(false);
        this.cargarRepartidores();
      });
    }
  }

  guardarRepartidor() {
    const editId = this.editingRepartidor()?.id;
    const request = editId
      ? this.repartidorService.actualizar(editId, this.repartidorForm)
      : this.repartidorService.crear(this.repartidorForm);

    request.subscribe(() => {
      this.isModalOpen.set(false);
      this.cargarRepartidores();
    });
  }

  toggleDisponibilidad(repartidor: RepartidorOutputDto, event: any) {
    
    event.stopPropagation();
    
    const isDisponible = event.detail.checked;
    
    this.repartidorService.actualizarDisponibilidad(repartidor.id, isDisponible).subscribe({
      next: () => {
        
        const actualizados = this.repartidores().map(r => 
          r.id === repartidor.id ? { ...r, disponible: isDisponible } : r
        );
        this.repartidores.set(actualizados);
      },
      error: () => {
        
        const prev = this.repartidores().map(r => r);
        this.repartidores.set(prev);
      }
    });
  }

  private getEmptyRepartidorForm(): any {
    return {
      clienteId: null,
      disponible: false,
    };
  }
}