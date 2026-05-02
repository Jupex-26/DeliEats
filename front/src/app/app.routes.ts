import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./layouts/home-component/home-component.component').then(m => m.HomeComponentComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];
