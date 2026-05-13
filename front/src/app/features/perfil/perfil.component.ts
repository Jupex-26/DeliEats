import { Component, inject, signal, OnInit, effect, untracked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonContent, IonIcon, IonToggle } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  personOutline,
  receiptOutline,
  arrowBackOutline,
  businessOutline,
  cameraOutline,
  bicycleOutline
} from 'ionicons/icons';
import { AuthService } from '../../services/auth/auth-service';
import { ClienteService } from '../../services/cliente/cliente-service';
import { RepartidorService } from '../../services/repartidor/repartidor-service';
import { ClienteOutputDto, RepartidorOutputDto } from '../../types';
import { PerfilEdicionComponent } from './perfil-edicion/perfil-edicion.component';
import { MisPedidosComponent } from './mis-pedidos/mis-pedidos.component';

type Tab = 'perfil' | 'pedidos' | 'repartidor';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, IonToggle, PerfilEdicionComponent, MisPedidosComponent],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  protected environment = environment;
  private authService = inject(AuthService);
  private clienteService = inject(ClienteService);
  private repartidorService = inject(RepartidorService);
  private router = inject(Router);

  activeTab = signal<Tab>('perfil');
  cliente = signal<ClienteOutputDto | null>(null);
  repartidor = signal<RepartidorOutputDto | null>(null);
  loading = signal(true);
  loadingRepartidor = signal(false);

  constructor() {
    addIcons({ personOutline, receiptOutline, arrowBackOutline, businessOutline, cameraOutline, bicycleOutline });

    // effect() reacciona de forma garantizada y automática cada vez que cambia el usuario actual en el AuthService
    effect(() => {
      const user = this.authService.currentUser();
      untracked(() => {
        if (user?.userOutputDto) {
          const rol = user.userOutputDto.nombreRol;
          const id = user.userOutputDto.id;
          if (rol === 'ROLE_CLIENTE') {
            this.loading.set(true);
            this.cliente.set(null); // fuerza la destrucción de subcomponentes para evitar retención de estado local
            this.clienteService.obtenerPorId(id).subscribe({
              next: (c) => {
                this.cliente.set(c);
                this.loading.set(false);
                if (this.activeTab() === 'repartidor') {
                  this.cargarRepartidor();
                }
              },
              error: () => this.loading.set(false)
            });
          } else {
            this.loading.set(false);
          }
        } else {
          this.cliente.set(null);
          this.repartidor.set(null);
          this.loading.set(true);
          this.activeTab.set('perfil');
        }
      });
    });
  }

  ngOnInit() {
    const user = this.authService.currentUser();
    if (!user?.userOutputDto) {
      this.router.navigate(['/login']);
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
    if (tab === 'repartidor') {
      this.cargarRepartidor();
    }
  }

  cargarRepartidor() {
    const id = this.cliente()?.id;
    if (!id) return;
    this.loadingRepartidor.set(true);
    this.repartidorService.obtenerPorId(id).subscribe({
      next: (rep) => {
        this.repartidor.set(rep);
        this.loadingRepartidor.set(false);
      },
      error: () => {
        this.loadingRepartidor.set(false);
      }
    });
  }

  toggleDisponibilidad(event: any) {
    const rep = this.repartidor();
    if (!rep) return;
    const isDisponible = event.detail ? event.detail.checked : event.target.checked;
    this.repartidorService.actualizarDisponibilidad(rep.id, isDisponible).subscribe({
      next: (updated) => {
        this.repartidor.set(updated);
      },
      error: () => {
        // revert local state on error
        this.repartidor.set({ ...rep });
      }
    });
  }

  onPerfilActualizado(data: any) {
    this.cliente.set(data);
  }

  volver() {
    this.router.navigate(['/restaurantes']);
  }
}


