import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { HeaderAdminComponent } from '../header-admin/header-admin.component';
import { AsideComponent } from '../aside/aside.component';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss'],
  standalone: true,
  imports: [IonicModule, RouterModule, HeaderAdminComponent, AsideComponent],
})
export class AdminLayoutComponent {
  
  isAsideOpen = false;

  toggleAside() {
    this.isAsideOpen = !this.isAsideOpen;
  }

  closeAside() {
    this.isAsideOpen = false;
  }
}