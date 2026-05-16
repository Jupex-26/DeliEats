import { Component, inject, OnInit, signal, effect, untracked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBackOutline,
  personOutline,
  receiptOutline,
  restaurantOutline,
  businessOutline
} from 'ionicons/icons';
import { AuthService } from '../../services/auth/auth-service';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { EmpresaOutputDto } from '../../types';
import { environment } from '../../../environments/environment';
import { EmpresaPerfilEdicionComponent } from './empresa-perfil-edicion/empresa-perfil-edicion.component';
import { EmpresaPedidosComponent } from './empresa-pedidos/empresa-pedidos.component';
import { EmpresaProductosComponent } from './empresa-productos/empresa-productos.component';

export type EmpresaTab = 'perfil' | 'pedidos' | 'productos';

@Component({
  selector: 'app-empresa-perfil',
  standalone: true,
  imports: [
    CommonModule,
    IonContent,
    IonIcon,
    EmpresaPerfilEdicionComponent,
    EmpresaPedidosComponent,
    EmpresaProductosComponent
  ],
  templateUrl: './empresa-perfil.component.html',
  styleUrls: ['./empresa-perfil.component.scss']
})
export class EmpresaPerfilComponent implements OnInit {
  protected environment = environment;
  private authService = inject(AuthService);
  private empresaService = inject(EmpresaService);
  private router = inject(Router);

  activeTab = signal<EmpresaTab>('perfil');
  empresa = signal<EmpresaOutputDto | null>(null);
  loading = signal(true);

  constructor() {
    addIcons({
      arrowBackOutline,
      personOutline,
      receiptOutline,
      restaurantOutline,
      businessOutline
    });

    effect(() => {
      const user = this.authService.currentUser();
      untracked(() => {
        if (user?.userOutputDto) {
          const id = user.userOutputDto.id;
          this.loading.set(true);
          this.empresa.set(null); 
          this.empresaService.obtenerPorId(id).subscribe({
            next: (e) => {
              this.empresa.set(e);
              this.loading.set(false);
            },
            error: () => this.loading.set(false)
          });
        } else {
          this.empresa.set(null);
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

  get usuario() {
    return this.authService.currentUser()?.userOutputDto ?? null;
  }

  getFotoUrl(): string | null {
    const foto = this.empresa()?.foto || this.usuario?.foto;
    if (!foto) return null;
    
    if (foto.startsWith('http')) return foto;
    return `${this.environment.storageUrl}/${foto}`;
  }

  onAvatarError(event: any) {
    console.error('[EmpresaPerfil] Error al cargar la foto:', event);
  }

  setTab(tab: EmpresaTab) {
    this.activeTab.set(tab);
  }

  onEmpresaActualizada(data: EmpresaOutputDto) {
    this.empresa.set(data);
  }

  volver() {
    this.router.navigate(['/restaurantes']);
  }
}
