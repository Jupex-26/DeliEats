import { Component, EventEmitter, inject, OnInit, Output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput, IonTextarea } from '@ionic/angular/standalone';
import { EmpresaInputDto, TipoCocinaOutputDto } from '../../types';
import { Validador } from '../../validadores/validador';
import { TipoCocinaService } from '../../services/tipococina/tipococina-service';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [IonItem, IonInput, IonTextarea, ReactiveFormsModule],
  templateUrl: './empresa-form.component.html',
  styleUrls: ['./empresa-form.component.scss'],
})
export class EmpresaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private tipoCocinaService = inject(TipoCocinaService);

  @Output() submitForm = new EventEmitter<EmpresaInputDto>();

  tiposCocina = signal<TipoCocinaOutputDto[]>([]);

  form = this.fb.group({
    // Campos de UserInputDto (Herencia)
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validators.required, Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    rolId: [3],

    // Campos de EmpresaInputDto
    descripcion: ['', [Validators.required]],
    correoContacto: ['', [Validators.required, Validators.email]],
    telefonoContacto: ['', [Validador.isTelefono]], // Opcional según tu Java
    tipoCocina: ['', [Validators.required]],
  });

  ngOnInit() {
    this.cargarTiposCocina();
  }

  cargarTiposCocina() {
    this.tipoCocinaService.listar(0, 100).subscribe({
      next: (res) => this.tiposCocina.set(res.content),
      error: (err) => console.error('Error al cargar tipos de cocina', err)
    });
  }

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
        rolId: 3,
      };

      this.submitForm.emit(empresaData);
    }
  }
}
