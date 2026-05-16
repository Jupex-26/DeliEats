import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { IonContent, IonInput, IonItem, IonSpinner } from '@ionic/angular/standalone';
import { AuthService } from '../../services/auth/auth-service';
import { LoginRequestDto, CustomError } from '../../types';
import { InfoModalComponent } from '../../shared/info-modal/info-modal.component';
import { finalize } from 'rxjs/operators';
import { Validador } from '../../validadores/validador';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [
    IonContent,
    IonInput,
    IonItem,
    IonSpinner,
    ReactiveFormsModule,
    RouterLink,
    InfoModalComponent,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  host: {
    class: 'ion-page', 
  },
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

    this.authService
      .login(credentials)
      .pipe(
        finalize(() => (this.isLoading = false)), 
      )
      .subscribe({
        next: (res) => {

          if (res.userOutputDto.nombreRol == 'ROLE_ADMIN') {
            this.router.navigate(['/admin/clientes']);
          } else {
            this.router.navigate(['/']);
          }

          this.cdr.detectChanges();
        },
        error: (err) => {
          
          this.modalTitle = 'Error de Autenticación';
          this.modalType = 'error';

          if (err.error && err.error.message) {
            console.log(err.error);
            
            this.errorData = err.error as CustomError;
            this.modalMessage = this.errorData.message;
            console.log(this.modalMessage);
          } else {
            
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