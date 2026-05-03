import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { IonContent, IonSpinner } from '@ionic/angular/standalone';
import { ClienteService } from '../../services/cliente/cliente-service';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { ClienteInputDto, EmpresaInputDto } from '../../types';
import { EmpresaFormComponent } from '../empresa-form/empresa-form.component';
import { UsuarioFormComponent } from '../usuario-form/usuario-form.component';

type RegistroType = 'seleccion' | 'cliente' | 'empresa';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [
    IonContent,
    IonSpinner,
    RouterLink,
    UsuarioFormComponent,
    EmpresaFormComponent,
    UsuarioFormComponent,
  ],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss'],
})
export class RegistroComponent {
  private clienteService = inject(ClienteService);
  private empresaService = inject(EmpresaService);
  private router = inject(Router);

  currentStep: RegistroType = 'seleccion';
  isLoading = false;
  errorMessage = '';

  setStep(step: RegistroType) {
    this.currentStep = step;
    this.errorMessage = '';
  }

  onClienteSubmit(data: ClienteInputDto) {
    this.isLoading = true;
    this.errorMessage = '';

    this.clienteService.crear(data).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Hubo un error al registrar el usuario. Revisa los datos o el correo.';
        console.error(err);
      },
    });
  }

  onEmpresaSubmit(data: EmpresaInputDto) {
    this.isLoading = true;
    this.errorMessage = '';

    this.empresaService.crear(data).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Hubo un error al registrar la empresa. Revisa los datos o el correo.';
        console.error(err);
      },
    });
  }
}
