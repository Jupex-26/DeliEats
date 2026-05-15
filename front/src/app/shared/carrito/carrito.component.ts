import { Component, Input, Output, EventEmitter, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  closeOutline,
  trashOutline,
  addOutline,
  removeOutline,
  cartOutline,
  checkmarkCircleOutline
} from 'ionicons/icons';
import { CarritoService } from '../../services/carrito/carrito-service';
import { EuroPipe } from '../../pipe/euro.pipe';
import { CarritoOutputDto, DetalleCarritoOutputDto, ProductoOutputDto } from '../../types';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, IonIcon, EuroPipe],
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.scss']
})
export class CarritoComponent implements OnInit {
  protected environment = environment;
  private carritoService = inject(CarritoService);
  private router = inject(Router);

  @Input() carritoId: number | null = null;
  @Input() productos: ProductoOutputDto[] = [];
  @Output() cerrar = new EventEmitter<void>();
  @Output() pedidoRealizado = new EventEmitter<void>();
  @Output() carritoActualizado = new EventEmitter<CarritoOutputDto>();

  carrito = this.carritoService.carrito;
  loading = signal(false);
  eliminando = signal<number | null>(null);

  constructor() {
    addIcons({
      closeOutline,
      trashOutline,
      addOutline,
      removeOutline,
      cartOutline,
      checkmarkCircleOutline
    });
  }

  ngOnInit() {
    if (this.carritoId) {
      this.cargarCarrito();
    }
  }

  cargarCarrito() {
    if (!this.carritoId) return;
    this.loading.set(true);
    this.carritoService.obtenerPorId(this.carritoId).subscribe({
      next: (c) => {
        this.carritoActualizado.emit(c);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  eliminarProducto(productoId: number) {
    if (!this.carritoId) return;
    this.eliminando.set(productoId);
    this.carritoService.eliminarProducto(this.carritoId, productoId).subscribe({
      next: () => {
        this.eliminando.set(null);
        this.cargarCarrito();
      },
      error: () => this.eliminando.set(null)
    });
  }

  actualizarCantidad(productoId: number, cantidad: number) {
    if (!this.carritoId) return;

    if (cantidad < 1) {
      this.eliminarProducto(productoId);
      return;
    }

    const producto = this.productos.find(p => p.id === productoId);
    if (producto && cantidad > producto.cantidad) {
      alert(`No hay más stock disponible de ${producto.nombre}`);
      return;
    }

    this.carritoService.actualizarCantidadProducto(this.carritoId, productoId, cantidad).subscribe({
      next: (c) => {
        this.carritoActualizado.emit(c);
      }
    });
  }

  vaciarCarrito() {
    if (!this.carritoId) return;
    this.carritoService.limpiar(this.carritoId).subscribe({
      next: () => {
        
      }
    });
  }

  get detalles(): DetalleCarritoOutputDto[] {
    return this.carrito()?.detalles ?? [];
  }

  get total(): number {
    return this.carrito()?.precioTotal ?? 0;
  }

  esStockMaximo(productoId: number, cantidadActual: number): boolean {
    const producto = this.productos.find(p => p.id === productoId);
    return producto ? cantidadActual >= producto.cantidad : false;
  }

  onCerrar() {
    this.cerrar.emit();
  }

  realizarPedido() {
    this.cerrar.emit();
    this.router.navigate(['/checkout']);
  }
}