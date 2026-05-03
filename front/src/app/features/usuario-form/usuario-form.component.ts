import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput, IonButton } from '@ionic/angular/standalone';
import { ClienteInputDto } from '../../types';
@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [IonItem, IonInput, IonButton, ReactiveFormsModule],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.scss'],
})
export class UsuarioFormComponent {
  private fb = inject(FormBuilder);

  @Output() submitForm = new EventEmitter<ClienteInputDto>();

  form = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    telefono: [null as number | null],
    direccion: [''],
    fechaNacimiento: ['', Validators.required],
    rolId: [2], // Asumiendo que 2 es el ROL_CLIENTE
  });

  onSubmit() {
    if (this.form.valid) {
      // Formatear la fecha a ISO string si es necesario, aquí lo pasamos tal cual el input date (YYYY-MM-DD)
      const value = this.form.value;

      const clienteData: ClienteInputDto = {
        nombre: value.nombre!,
        email: value.email!,
        password: value.password!,
        telefono: value.telefono ? Number(value.telefono) : undefined,
        direccion: value.direccion!,
        fechaNacimiento: value.fechaNacimiento ? new Date(value.fechaNacimiento).toISOString() : '',
        rolId: 2,
      };

      this.submitForm.emit(clienteData);
    }
  }
}
