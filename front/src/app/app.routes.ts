import { Routes } from '@angular/router';

// Layouts
import { PublicLayoutComponent } from './layouts/public-layout/public-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { HomeComponentComponent } from './layouts/home-component/home-component.component';

// Features
import { RestaurantesComponent } from './features/restaurantes/restaurantes.component';
import { LoginComponent } from './features/login-component/login.component';
import { RegistroComponent } from './features/registro/registro.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'registro',
    component: RegistroComponent,
  },
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      {
        path: '',
        component: HomeComponentComponent,
      },
      {
        path: 'restaurantes',
        component: RestaurantesComponent,
      },
    ],
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      // Aquí irán las rutas hijas del admin
    ],
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
