import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput } from '@ionic/angular/standalone';
import { ClienteInputDto } from '../../types';
import { Validador } from '../../validadores/validador';
@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [IonItem, IonInput, ReactiveFormsModule],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.scss'],
})
export class UsuarioFormComponent {
  private fb = inject(FormBuilder);

  readonly fechaLimite16 = new Date(new Date().setFullYear(new Date().getFullYear() - 16));

  @Output() submitForm = new EventEmitter<ClienteInputDto>();

  form = this.fb.group({
    // nombre: [valorInicial, [ValidadoresSíncronos], [ValidadoresAsíncronos]]
    nombre: ['', [Validators.required, Validador.isNombre]], // Agrupados en el 2º parámetro
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validador.isTelefono]], // Añade string vacío inicial
    direccion: ['', [Validators.required]],
    fechaNacimiento: ['', [Validators.required, Validador.before(this.fechaLimite16)]],
    rolId: [2],
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
