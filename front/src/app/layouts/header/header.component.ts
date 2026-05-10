import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  private authService = inject(AuthService);
  isMenuOpen = false;
  private router = inject(Router);

  // Usamos el signal del servicio directamente para reactividad total
  public currentUser = this.authService.currentUser;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout() {
    this.authService.logout();
    this.isMenuOpen = false;
    this.router.navigate(['/']);
  }

  isLogin() {
    return this.authService.isLogin();
  }

  getRol(): string | null {
    return this.authService.getRol();
  }

  getPerfilRoute(): string {
    return this.getRol() === 'ROLE_EMPRESA' ? '/empresa-perfil' : '/perfil';
  }
}
