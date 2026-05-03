import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { IonContent, IonInput, IonItem, IonButton, IonSpinner } from '@ionic/angular/standalone';
import { AuthService } from '../../services/auth/auth-service';
import { LoginRequestDto, CustomError } from '../../types';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';

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

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
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

    this.authService.login(credentials).subscribe({
      next: (res) => {
        this.isLoading = false;
        // Navigation on success
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;

        // Show error modal
        this.modalTitle = 'Error de Autenticación';
        this.modalType = 'error';

        if (err.error && err.error.message) {
          // Si es un CustomError que viene del backend
          this.errorData = err.error as CustomError;
          this.modalMessage = this.errorData.message;
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
      },
    });
  }

  onModalClosed() {
    this.isModalOpen = false;
  }
}
