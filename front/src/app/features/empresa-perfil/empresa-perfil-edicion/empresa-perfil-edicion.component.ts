import { Component, Input, Output, EventEmitter, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonIcon, IonItem, IonInput, IonTextarea, IonButton } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  saveOutline,
  checkmarkCircleOutline,
  lockClosedOutline,
  eyeOutline,
  eyeOffOutline,
  cameraOutline,
  timeOutline,
  addOutline,
  trashOutline
} from 'ionicons/icons';
import { EmpresaService } from '../../../services/empresa/empresa-service';
import { UserService } from '../../../services/user/user-service';
import { AuthService } from '../../../services/auth/auth-service';
import { TipoCocinaService } from '../../../services/tipococina/tipococina-service';
import { EmpresaOutputDto, EmpresaInputDto, TipoCocinaOutputDto } from '../../../types';
import { Validador } from '../../../validadores/validador';
import { InfoModalComponent } from '../../../shared/info-modal/info-modal.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-empresa-perfil-edicion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, IonIcon, IonItem, IonInput, IonTextarea, InfoModalComponent],
  templateUrl: './empresa-perfil-edicion.component.html',
  styleUrls: ['./empresa-perfil-edicion.component.scss']
})
export class EmpresaPerfilEdicionComponent implements OnInit {
  @Input({ required: true }) empresa!: EmpresaOutputDto;
  @Output() empresaActualizada = new EventEmitter<EmpresaOutputDto>();

  protected environment = environment;
  private fb = inject(FormBuilder);
  private empresaService = inject(EmpresaService);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private tipoCocinaService = inject(TipoCocinaService);

  tiposCocina = signal<TipoCocinaOutputDto[]>([]);
  guardando = signal(false);
  guardandoPassword = signal(false);
  guardandoFoto = signal(false);
  exitoPerfil = signal(false);
  exitoPassword = signal(false);
  exitoFoto = signal(false);
  mostrarPassword = signal(false);
  mostrarConfirm = signal(false);
  fotoPreview = signal<string | null>(null);
  fotoFile = signal<File | null>(null);

  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' | 'info' = 'info';

  perfilForm = this.fb.group({
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    descripcion: ['', [Validators.required]],
    correoContacto: ['', [Validators.required, Validators.email]],
    telefonoContacto: ['', [Validador.isTelefono]],
    tipoCocina: ['', [Validators.required]],
    aperturas: this.fb.array([])
  });

  get aperturas() {
    return this.perfilForm.get('aperturas') as FormArray;
  }

  passwordForm = this.fb.group({
    nuevaPassword: ['', [Validators.required, Validador.isStrongPassword]],
    confirmarPassword: ['', [Validators.required]]
  }, { validators: this.passwordsMatch });

  constructor() {
    addIcons({
      saveOutline,
      checkmarkCircleOutline,
      lockClosedOutline,
      eyeOutline,
      eyeOffOutline,
      cameraOutline,
      timeOutline,
      addOutline,
      trashOutline
    });
  }

  ngOnInit() {
    this.tipoCocinaService.listar(0, 100).subscribe({
      next: (res) => this.tiposCocina.set(res.content)
    });

    this.perfilForm.patchValue({
      nombre: this.empresa.nombre,
      email: this.empresa.email,
      telefono: this.empresa.telefono ? String(this.empresa.telefono) : '',
      direccion: this.empresa.direccion ?? '',
      descripcion: this.empresa.descripcion,
      correoContacto: this.empresa.correoContacto,
      telefonoContacto: this.empresa.telefonoContacto ?? '',
      tipoCocina: String(this.empresa.tipoCocina?.id ?? '')
    });

    // Cargar aperturas
    this.aperturas.clear();
    if (this.empresa.aperturas && this.empresa.aperturas.length > 0) {
      this.empresa.aperturas.forEach(a => {
        this.aperturas.push(this.fb.group({
          dia: [a.dia, Validators.required],
          horaApertura: [a.horaApertura, Validators.required],
          horaCierre: [a.horaCierre, Validators.required]
        }));
      });
    }

    if (this.empresa.foto) {
      this.fotoPreview.set(environment.storageUrl + '/' + this.empresa.foto);
    }
  }

  private passwordsMatch(group: any) {
    const nueva = group.get('nuevaPassword')?.value;
    const confirmar = group.get('confirmarPassword')?.value;
    return nueva === confirmar ? null : { noCoinciden: true };
  }

  private showError(title: string, err: any) {
    this.modalTitle = title;
    this.modalType = 'error';
    this.modalMessage = err?.error?.message ?? 'Ha ocurrido un error inesperado.';
    this.isModalOpen = true;
  }

  onModalClosed() { this.isModalOpen = false; }
  togglePassword() { this.mostrarPassword.update(v => !v); }
  toggleConfirm() { this.mostrarConfirm.update(v => !v); }

  agregarApertura() {
    this.aperturas.push(this.fb.group({
      dia: ['LUNES', Validators.required],
      horaApertura: ['09:00:00', Validators.required],
      horaCierre: ['22:00:00', Validators.required]
    }));
  }

  eliminarApertura(index: number) {
    this.aperturas.removeAt(index);
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

  guardarFoto() {
    const file = this.fotoFile();
    if (!file || this.guardandoFoto()) return;
    this.guardandoFoto.set(true);
    const fd = new FormData();
    fd.append('foto', file);
    this.userService.subirFoto(this.empresa.id!, fd).subscribe({
      next: (u) => {
        this.guardandoFoto.set(false);
        this.exitoFoto.set(true);
        const newFoto = u.foto;
        this.authService.updateUser({ foto: newFoto });
        if (newFoto) this.fotoPreview.set(environment.storageUrl + '/' + newFoto);
        this.fotoFile.set(null);
        this.empresaActualizada.emit({ ...this.empresa, foto: newFoto });
        setTimeout(() => this.exitoFoto.set(false), 3000);
      },
      error: (err) => { this.guardandoFoto.set(false); this.showError('Error al subir foto', err); }
    });
  }

  guardarPerfil() {
    if (this.perfilForm.invalid || this.guardando()) return;
    this.guardando.set(true);

    const v = this.perfilForm.getRawValue();
    const payload: EmpresaInputDto = {
      nombre: v.nombre!,
      email: v.email!,
      password: '',
      telefono: v.telefono ? Number(v.telefono) : undefined,
      direccion: v.direccion!,
      descripcion: v.descripcion!,
      correoContacto: v.correoContacto!,
      telefonoContacto: v.telefonoContacto ?? '',
      tipoCocina: v.tipoCocina!,
      aperturas: v.aperturas as any[],
      rolId: 3
    };

    this.empresaService.actualizar(this.empresa.id!, payload).subscribe({
      next: (updated) => {
        this.guardando.set(false);
        this.exitoPerfil.set(true);
        this.authService.updateUser({ nombre: updated.nombre, email: updated.email });
        this.empresaActualizada.emit(updated);
        setTimeout(() => this.exitoPerfil.set(false), 3000);
      },
      error: (err) => { this.guardando.set(false); this.showError('Error al actualizar perfil', err); }
    });
  }

  guardarPassword() {
    if (this.passwordForm.invalid || this.guardandoPassword()) return;
    this.guardandoPassword.set(true);
    const pwd = this.passwordForm.value.nuevaPassword!;
    this.userService.actualizarPassword(this.empresa.id!, pwd).subscribe({
      next: () => {
        this.guardandoPassword.set(false);
        this.exitoPassword.set(true);
        this.passwordForm.reset();
        setTimeout(() => this.exitoPassword.set(false), 3000);
      },
      error: (err) => { this.guardandoPassword.set(false); this.showError('Error al cambiar contraseña', err); }
    });
  }
}
