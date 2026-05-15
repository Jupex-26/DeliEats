import { Component, inject } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { AuthService } from '../../services/auth/auth-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header-admin',
  templateUrl: './header-admin.component.html',
  styleUrls: ['./header-admin.component.scss'],
  standalone: true,
  imports: [IonicModule, RouterLink],
})
export class HeaderAdminComponent {
  public authService = inject(AuthService);
  private router = inject(Router);
  isMenuOpen = false;
  public user = this.authService.currentUser()?.userOutputDto;
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
    
  }

  protected isLogin() {
    return this.authService.isLogin();
  }
}