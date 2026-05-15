import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { IonContent, IonSpinner } from '@ionic/angular/standalone';
import { ClienteService } from '../../services/cliente/cliente-service';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { ClienteInputDto, EmpresaInputDto, CustomError } from '../../types';
import { EmpresaFormComponent } from '../empresa-form/empresa-form.component';
import { UsuarioFormComponent } from '../usuario-form/usuario-form.component';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';

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
    InfoModalComponent
  ],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss'],
})
export class RegistroComponent {
  private clienteService = inject(ClienteService);
  private empresaService = inject(EmpresaService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  currentStep: RegistroType = 'seleccion';
  isLoading = false;

  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' | 'info' = 'info';
  errorData: CustomError | null = null;

  private navigateAfterClose = false;

  setStep(step: RegistroType) {
    this.currentStep = step;
  }

  onClienteSubmit(data: ClienteInputDto) {
    this.isLoading = true;

    this.clienteService.crear(data).subscribe({
      next: () => {
        this.isLoading = false;
        this.showSuccessModal('Usuario registrado correctamente. Ahora puedes iniciar sesión.');
        this.cdr.detectChanges();
        this.currentStep = 'seleccion';
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Error al registrar usuario', err);
        this.cdr.detectChanges();
      },
    });
  }

  onEmpresaSubmit(data: EmpresaInputDto) {
    this.isLoading = true;

    this.empresaService.crear(data).subscribe({
      next: () => {
        this.isLoading = false;
        this.showSuccessModal('Empresa registrada correctamente. Ahora puedes iniciar sesión.');
        this.currentStep = 'seleccion';
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Error al registrar empresa', err);
        this.cdr.detectChanges();
      },
    });
  }

  private showSuccessModal(message: string) {
    this.modalTitle = '¡Registro Completado!';
    this.modalMessage = message;
    this.modalType = 'success';
    this.errorData = null;
    this.isModalOpen = true;
    this.navigateAfterClose = true;
  }

  private showErrorModal(title: string, err: any) {
    this.modalTitle = title;
    this.modalType = 'error';
    this.navigateAfterClose = false;

    if (err.error && err.error.message) {
      this.errorData = err.error as CustomError;
      this.modalMessage = this.errorData.message;
    } else {
      this.errorData = null;
      this.modalMessage = 'Hubo un error inesperado al conectar con el servidor.';
    }

    this.isModalOpen = true;
  }

  onModalClosed() {
    this.isModalOpen = false;
    if (this.navigateAfterClose) {
      this.router.navigate(['/login']);
    }
    this.cdr.detectChanges();
  }
}