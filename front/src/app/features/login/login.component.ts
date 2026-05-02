import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IonContent, IonInput, IonItem, IonButton, IonSpinner } from '@ionic/angular/standalone';
import { AuthService } from '../../services/auth/auth-service';
import { LoginRequestDto } from '../../types';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [IonContent, IonInput, IonItem, IonButton, IonSpinner, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  isLoading = false;
  errorMessage = '';

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const credentials: LoginRequestDto = {
      email: this.loginForm.value.email as string,
      password: this.loginForm.value.password as string
    };

    this.authService.login(credentials).subscribe({
      next: (res) => {
        this.isLoading = false;
        // Aquí guardarías el token, ej: localStorage.setItem('token', res.token);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Credenciales incorrectas o error en el servidor.';
        console.error(err);
      }
    });
  }
}
