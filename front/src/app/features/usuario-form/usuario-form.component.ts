import { Component, EventEmitter, inject, Input, Output, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput } from '@ionic/angular/standalone';
import { ClienteInputDto, ClienteOutputDto } from '../../types';
import { Validador } from '../../validadores/validador';
import { GeocodingService } from '../../services/geocoding/geocoding-service';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [IonItem, IonInput, ReactiveFormsModule],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.scss'],
})
export class UsuarioFormComponent implements OnInit, OnChanges {
  private fb = inject(FormBuilder);
  private geocodingService = inject(GeocodingService);
  validandoDireccion = false;
  errorDireccion = false;

  readonly fechaLimite16 = new Date(new Date().setFullYear(new Date().getFullYear() - 16));

  @Input() mode: 'register' | 'edit' = 'register';
  @Input() userData?: ClienteOutputDto;
  @Output() submitForm = new EventEmitter<ClienteInputDto>();

  form = this.fb.group({
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    fechaNacimiento: ['', [Validators.required, Validador.before(this.fechaLimite16)]],
    rolId: [2],
  });

  ngOnInit() {
    this.updateFormMode();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['userData'] && this.userData) {
      this.patchForm();
    }
    if (changes['mode']) {
      this.updateFormMode();
    }
  }

  private updateFormMode() {
    const passwordControl = this.form.get('password');
    if (this.mode === 'edit') {
      passwordControl?.clearValidators();
      passwordControl?.updateValueAndValidity();
    } else {
      passwordControl?.setValidators([Validators.required, Validador.isStrongPassword]);
      passwordControl?.updateValueAndValidity();
    }
  }

  private patchForm() {
    if (!this.userData) return;
    
    this.form.patchValue({
      nombre: this.userData.nombre,
      email: this.userData.email,
      telefono: this.userData.telefono?.toString() || '',
      direccion: this.userData.direccion || '',
      fechaNacimiento: this.userData.fechaNacimiento ? this.userData.fechaNacimiento.split('T')[0] : ''
    });
    this.form.markAsPristine();
  }

  onSubmit() {
    if (this.form.valid) {
      const value = this.form.value;
      const direccion = value.direccion!;

      this.validandoDireccion = true;
      this.errorDireccion = false;

      this.geocodingService.verificarDireccion(direccion).subscribe(isValid => {
        this.validandoDireccion = false;
        
        if (!isValid) {
          this.errorDireccion = true;
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

        this.submitForm.emit(clienteData);
      });
    }
  }
}