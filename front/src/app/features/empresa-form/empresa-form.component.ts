import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, OnInit, Output, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { IonItem, IonInput, IonTextarea, IonButton, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { addCircleOutline, trashOutline, eyeOutline, eyeOffOutline } from 'ionicons/icons';
import { EmpresaInputDto, TipoCocinaOutputDto } from '../../types';
import { Validador } from '../../validadores/validador';
import { TipoCocinaService } from '../../services/tipococina/tipococina-service';
import { GeocodingService } from '../../services/geocoding/geocoding-service';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [CommonModule, IonItem, IonInput, IonTextarea, IonButton, IonIcon, ReactiveFormsModule],
  templateUrl: './empresa-form.component.html',
  styleUrls: ['./empresa-form.component.scss'],
})
export class EmpresaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private tipoCocinaService = inject(TipoCocinaService);
  private geocodingService = inject(GeocodingService);

  validandoDireccion = signal(false);
  errorDireccion = signal(false);
  showPassword = signal(false);

  @Output() submitForm = new EventEmitter<EmpresaInputDto>();

  tiposCocina = signal<TipoCocinaOutputDto[]>([]);

  diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  form = this.fb.group({
    nombre: ['', [Validators.required, Validador.isNombre]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
    telefono: ['', [Validators.required, Validador.isTelefono]],
    direccion: ['', [Validators.required]],
    rolId: [3],
    descripcion: ['', [Validators.required]],
    correoContacto: ['', [Validators.required, Validators.email]],
    telefonoContacto: ['', [Validador.isTelefono]], 
    tipoCocinaId: ['', [Validators.required]],
    aperturas: this.fb.array([], [Validators.required, Validators.minLength(1)])
  });

  constructor() {
    addIcons({ addCircleOutline, trashOutline, eyeOutline, eyeOffOutline });
  }

  get aperturas() {
    return this.form.get('aperturas') as FormArray;
  }

  addApertura() {
    const aperturaGroup = this.fb.group({
      dia: ['LUNES', Validators.required],
      horaApertura: ['09:00', Validators.required],
      horaCierre: ['22:00', Validators.required]
    });
    this.aperturas.push(aperturaGroup);
  }

  removeApertura(index: number) {
    this.aperturas.removeAt(index);
  }

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
      const direccion = value.direccion!;

      this.validandoDireccion.set(true);
      this.errorDireccion.set(false);

      this.geocodingService.verificarDireccion(direccion).subscribe(isValid => {
        this.validandoDireccion.set(false);
        
        if (!isValid) {
          this.errorDireccion.set(true);
          return;
        }

        const empresaData: EmpresaInputDto = {
          nombre: value.nombre!,
          email: value.email!,
          password: value.password!,
          telefono: value.telefono ? Number(value.telefono) : undefined,
          direccion: direccion,
          descripcion: value.descripcion!,
          correoContacto: value.correoContacto!,
          telefonoContacto: value.telefonoContacto!,
          tipoCocinaId: Number(value.tipoCocinaId!),
          rolId: 3,
          aperturas: value.aperturas as any[]
        };

        this.submitForm.emit(empresaData);
      });
    }
  }
}