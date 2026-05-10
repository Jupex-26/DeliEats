import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  personOutline,
  receiptOutline,
  arrowBackOutline,
  businessOutline,
  cameraOutline
} from 'ionicons/icons';
import { AuthService } from '../../services/auth/auth-service';
import { ClienteService } from '../../services/cliente/cliente-service';
import { ClienteOutputDto } from '../../types';
import { PerfilEdicionComponent } from './perfil-edicion/perfil-edicion.component';
import { MisPedidosComponent } from './mis-pedidos/mis-pedidos.component';

type Tab = 'perfil' | 'pedidos';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, PerfilEdicionComponent, MisPedidosComponent],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  protected environment = environment;
  private authService = inject(AuthService);
  private clienteService = inject(ClienteService);
  private router = inject(Router);

  activeTab = signal<Tab>('perfil');
  cliente = signal<ClienteOutputDto | null>(null);
  loading = signal(true);

  constructor() {
    addIcons({ personOutline, receiptOutline, arrowBackOutline, businessOutline, cameraOutline });
  }

  ngOnInit() {
    const user = this.authService.currentUser();
    if (!user?.userOutputDto) {
      this.router.navigate(['/login']);
      return;
    }

    const rol = this.authService.getRol();
    const id = user.userOutputDto.id;

    if (rol === 'ROLE_CLIENTE') {
      this.clienteService.obtenerPorId(id).subscribe({
        next: (c) => {
          this.cliente.set(c);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    } else {
      this.loading.set(false);
    }
  }

  get rol(): string {
    return this.authService.getRol() ?? '';
  }

  get usuario() {
    return this.authService.currentUser()?.userOutputDto ?? null;
  }

  setTab(tab: Tab) {
    this.activeTab.set(tab);
  }

  onPerfilActualizado(data: any) {
    this.cliente.set(data);
  }

  volver() {
    this.router.navigate(['/restaurantes']);
  }
}
