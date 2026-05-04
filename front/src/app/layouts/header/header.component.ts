import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  public authService = inject(AuthService);
  isMenuOpen = false;
  public user = this.authService.currentUser().userOutputDto;
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout() {
    this.authService.logout();
    // Opcional: redirigir al inicio tras cerrar sesión
  }

  protected isLogin() {
    return this.authService.isLogin();
  }
}
