import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { IonContent, IonInput, IonItem, IonButton, IonSpinner } from '@ionic/angular/standalone';
import { AuthService } from '../../services/auth/auth-service';
import { LoginRequestDto, CustomError } from '../../types';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';
import { finalize } from 'rxjs/operators';
import { Validador } from '../../validadores/validador'; // Importar finalize

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [IonContent, IonInput, IonItem, IonButton, IonSpinner, ReactiveFormsModule, RouterLink, InfoModalComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validador.isStrongPassword]],
  });

  isLoading = false;

  // Modal state
  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  modalType: 'success' | 'error' | 'info' = 'error';
  errorData: CustomError | null = null;

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;

    const credentials: LoginRequestDto = {
      email: this.loginForm.value.email as string,
      password: this.loginForm.value.password as string,
    };

    this.authService.login(credentials).pipe(
      finalize(() => this.isLoading = false) // Asegura que isLoading siempre se ponga en false al finalizar
    ).subscribe({
      next: () => {
        // Navigation on success
        this.router.navigate(['/']);
        this.cdr.detectChanges();
      },
      error: (err) => {
        // isLoading ya se maneja con finalize, así que lo quitamos de aquí
        // this.isLoading = false;

        // Show error modal
        this.modalTitle = 'Error de Autenticación';
        this.modalType = 'error';

        if (err.error && err.error.message) {
          console.log(err.error);
          // Si es un CustomError que viene del backend
          this.errorData = err.error as CustomError;
          this.modalMessage = this.errorData.message;
          console.log(this.modalMessage);
        } else {
          // Error genérico o de red
          this.errorData = null;
          if (err.status === 401 || err.status === 403) {
            this.modalMessage = 'Correo o contraseña incorrectos.';
          } else {
            this.modalMessage = 'Hubo un error al conectar con el servidor. Inténtalo más tarde.';
          }
        }

        this.isModalOpen = true;
        this.cdr.detectChanges();
      },
    });
  }

  onModalClosed() {
    this.isModalOpen = false;
    this.cdr.detectChanges();
  }
}
