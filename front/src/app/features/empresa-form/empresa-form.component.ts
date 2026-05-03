import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput, IonTextarea, IonButton } from '@ionic/angular/standalone';
import { EmpresaInputDto } from '../../types';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [IonItem, IonInput, IonTextarea, IonButton, ReactiveFormsModule],
  templateUrl: './empresa-form.component.html',
  styleUrls: ['./empresa-form.component.scss']
})
export class EmpresaFormComponent {
  private fb = inject(FormBuilder);

  @Output() submitForm = new EventEmitter<EmpresaInputDto>();

  form = this.fb.group({
    nombre: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    telefono: [null as number | null],
    direccion: [''],
    descripcion: ['', Validators.required],
    correoContacto: ['', Validators.email],
    telefonoContacto: [''],
    tipoCocina: ['', Validators.required],
    rolId: [3] // Asumiendo que 3 es el ROL_EMPRESA
  });

  onSubmit() {
    if (this.form.valid) {
      const value = this.form.value;

      const empresaData: EmpresaInputDto = {
        nombre: value.nombre!,
        email: value.email!,
        password: value.password!,
        telefono: value.telefono ? Number(value.telefono) : undefined,
        direccion: value.direccion!,
        descripcion: value.descripcion!,
        correoContacto: value.correoContacto!,
        telefonoContacto: value.telefonoContacto!,
        tipoCocina: value.tipoCocina!,
        rolId: 3
      };

      this.submitForm.emit(empresaData);
    }
  }
}
