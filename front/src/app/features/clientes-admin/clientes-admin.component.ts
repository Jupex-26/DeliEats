import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  calendarOutline,
  locationOutline
} from 'ionicons/icons';
import { ClienteService } from '../../services/cliente/cliente-service';
import { ClienteOutputDto, ClienteInputDto } from '../../types';
import { ConfirmModalComponent } from '../../shared/confirm-modal/confirm-modal.component';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { DatePipe } from '@angular/common';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-clientes-admin',
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
    DatePipe,
  ],
  templateUrl: './clientes-admin.component.html',
  styleUrls: ['./clientes-admin.component.scss'],
})
export class ClientesAdminComponent implements OnInit {
  private clienteService = inject(ClienteService);

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

  private debouncer = new Subject<string>();
  clienteForm: ClienteInputDto = this.getEmptyForm();

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
      calendarOutline,
      locationOutline
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

  abrirModalNuevo() {
    this.editingCliente.set(null);
    this.clienteForm = this.getEmptyForm();
    this.isModalOpen.set(true);
  }

  abrirModalEditar(cliente: ClienteOutputDto) {
    this.editingCliente.set(cliente);
    this.clienteForm = { ...cliente, rolId: 2 };
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
    const editId = this.editingCliente()?.id;
    const request = editId
      ? this.clienteService.actualizar(editId, this.clienteForm)
      : this.clienteService.crear(this.clienteForm);

    request.subscribe(() => {
      this.isModalOpen.set(false);
      this.cargarClientes();
    });
  }

  private getEmptyForm(): ClienteInputDto {
    return {
      nombre: '',
      email: '',
      telefono: undefined,
      direccion: '',
      foto: '',
      rolId: 2,
      fechaNacimiento: '',
    };
  }
}
