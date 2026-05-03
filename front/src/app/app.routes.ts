import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./layouts/public-layout/public-layout.component').then(
        (m) => m.PublicLayoutComponent,
      ),
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./layouts/home-component/home-component.component').then(
            (m) => m.HomeComponentComponent,
          ),
      },
      {
        path: 'restaurantes',
        loadComponent: () =>
          import('./features/restaurantes/restaurantes.component').then(
            (m) => m.RestaurantesComponent,
          ),
      },
    ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/login-component/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'registro',
    loadComponent: () =>
      import('./features/registro/registro.component').then((m) => m.RegistroComponent),
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
