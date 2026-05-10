import { Component, Input, OnInit, inject, signal, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  cameraOutline,
  lockClosedOutline,
  checkmarkCircleOutline,
  saveOutline,
  eyeOutline,
  eyeOffOutline
} from 'ionicons/icons';
import { ClienteService } from '../../../services/cliente/cliente-service';
import { UserService } from '../../../services/user/user-service';
import { AuthService } from '../../../services/auth/auth-service';
import { ClienteOutputDto, ClienteInputDto } from '../../../types';
import { Validador } from '../../../validadores/validador';
import { UsuarioFormComponent } from '../../usuario-form/usuario-form.component';

import { CustomError } from '../../../types';
import { InfoModalComponent } from '../../../shared/info-modal/info-modal.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-perfil-edicion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IonItem, IonInput, IonIcon, UsuarioFormComponent, InfoModalComponent],
  templateUrl: './perfil-edicion.component.html',
  styleUrls: ['./perfil-edicion.component.scss']
})
export class PerfilEdicionComponent implements OnInit {
  @Input({ required: true }) cliente!: ClienteOutputDto;
  @Output() perfilActualizado = new EventEmitter<any>();

  private fb = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private userService = inject(UserService);
  private authService = inject(AuthService);

  guardando = signal(false);
  guardandoPassword = signal(false);
  guardandoFoto = signal(false);
  exitoPerfil = signal(false);
  exitoPassword = signal(false);
  exitoFoto = signal(false);

  // Modal State
  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' | 'info' = 'info';
  errorData: CustomError | null = null;

  mostrarPassword = signal(false);
  mostrarConfirm = signal(false);
  fotoPreview = signal<string | null>(null);
  fotoFile = signal<File | null>(null);

  passwordForm = this.fb.group({
    nuevaPassword: ['', [Validators.required, Validador.isStrongPassword]],
    confirmarPassword: ['', [Validators.required]],
  }, { validators: this.passwordsMatch });

  protected environment = environment;

  constructor() {
    addIcons({ cameraOutline, lockClosedOutline, checkmarkCircleOutline, saveOutline, eyeOutline, eyeOffOutline });
  }

  ngOnInit() {
    if (this.cliente.foto) {
      this.fotoPreview.set(environment.storageUrl + '/' + this.cliente.foto);
    }
  }

  private showErrorModal(title: string, err: any) {
    this.modalTitle = title;
    this.modalType = 'error';

    if (err.error && err.error.message) {
      this.errorData = err.error as CustomError;
      this.modalMessage = this.errorData.message;
    } else {
      this.errorData = null;
      this.modalMessage = 'Hubo un error inesperado al procesar tu solicitud.';
    }

    this.isModalOpen = true;
  }

  onModalClosed() {
    this.isModalOpen = false;
  }

  private passwordsMatch(group: any) {
    const nueva = group.get('nuevaPassword')?.value;
    const confirmar = group.get('confirmarPassword')?.value;
    return nueva === confirmar ? null : { noCoinciden: true };
  }

  onFotoSeleccionada(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.showErrorModal('Archivo inválido', { error: { message: 'El archivo seleccionado debe ser una imagen.' } });
      return;
    }

    this.fotoFile.set(file);
    const reader = new FileReader();
    reader.onload = (e) => this.fotoPreview.set(e.target?.result as string);
    reader.readAsDataURL(file);
  }

  guardarFoto() {
    const file = this.fotoFile();
    if (!file || this.guardandoFoto()) return;

    this.guardandoFoto.set(true);

    const formData = new FormData();
    formData.append('foto', file);

    this.userService.subirFoto(this.cliente.id, formData).subscribe({
      next: (updatedUser) => {
        this.guardandoFoto.set(false);
        this.exitoFoto.set(true);
        const newFoto = updatedUser.foto;
        this.authService.updateUser({ foto: newFoto });
        
        // Actualizamos el preview para que use la URL del servidor
        if (newFoto) {
          this.fotoPreview.set(environment.storageUrl + '/' + newFoto);
        }
        
        this.perfilActualizado.emit({ ...this.cliente, foto: newFoto });
        this.fotoFile.set(null); // Limpiamos el archivo seleccionado
        setTimeout(() => this.exitoFoto.set(false), 3000);
      },
      error: (err) => {
        this.guardandoFoto.set(false);
        this.showErrorModal('Error al subir la foto', err);
      }
    });
  }

  guardarPerfil(payload: ClienteInputDto) {
    if (this.guardando()) return;
    this.guardando.set(true);

    this.clienteService.actualizar(this.cliente.id, payload).subscribe({
      next: (updatedCliente) => {
        this.guardando.set(false);
        this.exitoPerfil.set(true);
        this.authService.updateUser({ 
          nombre: updatedCliente.nombre, 
          email: updatedCliente.email,
          direccion: updatedCliente.direccion
        });
        this.perfilActualizado.emit(updatedCliente);
        setTimeout(() => this.exitoPerfil.set(false), 3000);
      },
      error: (err) => {
        this.guardando.set(false);
        this.showErrorModal('Error al actualizar perfil', err);
      }
    });
  }

  guardarPassword() {
    if (this.passwordForm.invalid || this.guardandoPassword()) return;
    this.guardandoPassword.set(true);

    const nuevaPassword = this.passwordForm.value.nuevaPassword!;

    this.userService.actualizarPassword(this.cliente.id, nuevaPassword).subscribe({
      next: () => {
        this.guardandoPassword.set(false);
        this.exitoPassword.set(true);
        this.passwordForm.reset();
        setTimeout(() => this.exitoPassword.set(false), 3000);
      },
      error: (err) => {
        this.guardandoPassword.set(false);
        this.showErrorModal('Error al cambiar contraseña', err);
      }
    });
  }

  togglePassword() { this.mostrarPassword.update(v => !v); }
  toggleConfirm() { this.mostrarConfirm.update(v => !v); }
}
