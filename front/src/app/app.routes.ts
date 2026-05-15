import { Routes } from '@angular/router';

import { PublicLayoutComponent } from './layouts/public-layout/public-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { HomeComponentComponent } from './layouts/home-component/home-component.component';

import { RestaurantesComponent } from './features/restaurantes/restaurantes.component';
import { LoginComponent } from './features/login-component/login.component';
import { RegistroComponent } from './features/registro/registro.component';
import { ClientesAdminComponent } from './features/clientes-admin/clientes-admin.component';
import { RestaurantesAdminComponent } from './features/restaurantes-admin/restaurantes-admin.component';
import { RepartidoresAdminComponent } from './features/repartidores-admin/repartidores-admin.component';

import { roleGuard } from './guards/role-guard';

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
      {
        path: 'restaurantes/:id',
        loadComponent: () => import('./features/restaurante-cliente/restaurante-cliente.component').then(m => m.RestauranteClienteComponent)
      },
      {
        path: 'checkout',
        loadComponent: () => import('./features/checkout/checkout.component').then(m => m.CheckoutComponent)
      },
      {
        path: 'privacidad',
        loadComponent: () => import('./layouts/legal/privacidad/privacidad.component').then(m => m.PrivacidadComponent)
      },
      {
        path: 'terminos',
        loadComponent: () => import('./layouts/legal/terminos/terminos.component').then(m => m.TerminosComponent)
      },
      {
        path: 'perfil',
        loadComponent: () => import('./features/perfil/perfil.component').then(m => m.PerfilComponent),
        canActivate: [roleGuard],
        data: { role: ['ROLE_CLIENTE', 'ROLE_REPARTIDOR'] }
      },
      {
        path: 'empresa-perfil',
        loadComponent: () => import('./features/empresa-perfil/empresa-perfil.component').then(m => m.EmpresaPerfilComponent),
        canActivate: [roleGuard],
        data: { role: ['ROLE_EMPRESA'] }
      },
      {
        path: 'pedidos/:id',
        loadComponent: () => import('./features/perfil/detalle-pedido-cliente/detalle-pedido-cliente.component')
          .then(m => m.DetallePedidoClienteComponent),
        canActivate: [roleGuard],
        data: { role: ['ROLE_CLIENTE'] }
      },
    ],
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      {
        path: 'clientes',
        component: ClientesAdminComponent
      },
      {
        path: 'restaurantes',
        component: RestaurantesAdminComponent
      },
      {
        path: 'repartidores',
        component: RepartidoresAdminComponent
      },
      {
        path: 'pedidos',
        loadComponent: () => import('./features/pedidos-admin/pedidos-admin.component').then(m => m.PedidosAdminComponent)
      }
    ],
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];