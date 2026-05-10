import { Component, OnInit, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBackOutline,
  checkmarkCircleOutline,
  closeCircleOutline,
  receiptOutline,
  cartOutline,
  walletOutline,
  arrowForwardOutline
} from 'ionicons/icons';
import { CarritoService } from '../../services/carrito/carrito-service';
import { CheckoutService } from '../../services/checkout/checkout-service';
import { AuthService } from '../../services/auth/auth-service';
import { EuroPipe } from '../../pipe/euro.pipe';
import { CarritoOutputDto } from '../../types';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, EuroPipe],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  protected environment = environment;
  private router = inject(Router);
  private carritoService = inject(CarritoService);
  private checkoutService = inject(CheckoutService);
  private authService = inject(AuthService);

  carrito = signal<CarritoOutputDto | null>(null);
  loading = signal(true);
  procesando = signal(false);
  pedidoRealizado = signal(false);
  error = signal<string | null>(null);

  constructor() {
    addIcons({
      arrowBackOutline,
      checkmarkCircleOutline,
      closeCircleOutline,
      receiptOutline,
      cartOutline,
      walletOutline,
      arrowForwardOutline
    });
  }

  ngOnInit() {
    const clienteId = this.authService.currentUser()?.userOutputDto?.id;
    if (!clienteId) {
      this.router.navigate(['/login']);
      return;
    }

    this.carritoService.obtenerPorUsuario(clienteId).subscribe({
      next: (c) => {
        this.carrito.set(c);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el carrito.');
        this.loading.set(false);
      }
    });
  }

  get detalles() {
    return this.carrito()?.detalles ?? [];
  }

  get total() {
    return this.carrito()?.precioTotal ?? 0;
  }

  procesarPago() {
    if (this.procesando()) return;
    this.procesando.set(true);
    this.error.set(null);

    this.checkoutService.finalizarCompra().subscribe({
      next: () => {
        this.procesando.set(false);
        this.pedidoRealizado.set(true);
        this.carritoService.limpiarEstadoLocal();
        setTimeout(() => this.router.navigate(['/restaurantes']), 3000);
      },
      error: () => {
        this.procesando.set(false);
        this.error.set('Error al procesar el pago. Inténtalo de nuevo.');
      }
    });
  }

  cancelar() {
    history.back();
  }
}
