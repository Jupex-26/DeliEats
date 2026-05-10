import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBackOutline,
  cartOutline,
  addCircleOutline,
  callOutline,
  mailOutline,
  locationOutline,
  timeOutline,
  restaurantOutline,
  starOutline,
  checkmarkCircleOutline,
  arrowForwardOutline
} from 'ionicons/icons';
import { EmpresaService } from '../../services/empresa/empresa-service';
import { ProductoService } from '../../services/producto/producto-service';
import { CarritoService } from '../../services/carrito/carrito-service';
import { AuthService } from '../../services/auth/auth-service';
import { CarritoComponent } from '../../shared/carrito/carrito.component';
import { HoraPipe } from '../../pipe/hora.pipe';
import { AperturaHoyPipe } from '../../pipe/apertura-hoy.pipe';
import { EuroPipe } from '../../pipe/euro.pipe';
import { EmpresaOutputDto, ProductoOutputDto, CarritoOutputDto } from '../../types';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-restaurante-cliente',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, EuroPipe, CarritoComponent, HoraPipe, AperturaHoyPipe],
  templateUrl: './restaurante-cliente.component.html',
  styleUrls: ['./restaurante-cliente.component.scss']
})
export class RestauranteClienteComponent implements OnInit {
  protected environment = environment;
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private empresaService = inject(EmpresaService);
  private productoService = inject(ProductoService);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);

  empresa = signal<EmpresaOutputDto | null>(null);
  productos = signal<ProductoOutputDto[]>([]);
  carrito = this.carritoService.carrito;
  carritoId = signal<number | null>(null);

  loadingEmpresa = signal(true);
  loadingProductos = signal(true);
  loadingCarrito = signal(false);
  cartOpen = signal(false);
  addingProductId = signal<number | null>(null);
  addedProductId = signal<number | null>(null);
  showAllHorarios = signal(false);

  /** Nombre del día actual en MAYÚSCULAS y SIN ACENTOS (formato back) */
  readonly diaHoy = (() => {
    const dias = ['DOMINGO', 'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
    return dias[new Date().getDay()];
  })();

  /** Lista ordenada de días para mostrar en el horario completo */
  readonly diasSemanaList = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  getAperturaPorDia(dia: string) {
    return this.empresa()?.aperturas.find(a => a.dia === dia);
  }

  /** Cálculo reactivo del total de productos en el carrito */
  totalProductosCarrito = computed(() => {
    const detalles = this.carrito()?.detalles || [];
    return detalles.reduce((sum, d) => sum + d.cantidad, 0);
  });

  /** Mapa reactivo de ID de producto -> Cantidad en el carrito */
  cantidadesEnCarrito = computed(() => {
    const mapa = new Map<number, number>();
    this.carrito()?.detalles.forEach(d => {
      mapa.set(d.productoId, d.cantidad);
    });
    return mapa;
  });

  constructor() {
    addIcons({
      arrowBackOutline,
      cartOutline,
      addCircleOutline,
      callOutline,
      mailOutline,
      locationOutline,
      timeOutline,
      restaurantOutline,
      starOutline,
      checkmarkCircleOutline,
      arrowForwardOutline
    });
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/restaurantes']);
      return;
    }
    this.cargarEmpresa(id);
    this.cargarCarritoUsuario();
  }

  private cargarEmpresa(id: number) {
    this.loadingEmpresa.set(true);
    this.loadingProductos.set(true);
    this.empresaService.obtenerPorId(id).subscribe({
      next: (e) => {
        this.empresa.set(e);
        this.productos.set(e.productos || []);
        this.loadingEmpresa.set(false);
        this.loadingProductos.set(false);
      },
      error: () => {
        this.loadingEmpresa.set(false);
        this.loadingProductos.set(false);
        this.router.navigate(['/restaurantes']);
      }
    });
  }


  private cargarCarritoUsuario() {
    const user = this.authService.currentUser();
    if (!user?.userOutputDto?.id) return;
    const clienteId = user.userOutputDto.id;
    this.carritoService.obtenerPorUsuario(clienteId).subscribe({
      next: (c) => {
        this.carritoId.set(c.id);
      },
      error: () => {
        // No tiene carrito activo, es normal
      }
    });
  }

  agregarAlCarrito(producto: ProductoOutputDto) {
    if (!this.authService.isLogin()) {
      this.router.navigate(['/login']);
      return;
    }

    const clienteId = this.authService.currentUser()?.userOutputDto?.id;
    if (!clienteId || this.loadingCarrito()) return;

    this.loadingCarrito.set(true);
    this.addingProductId.set(producto.id);

    // Si ya conocemos el carrito, vamos directo a actualizar
    if (this.carritoId()) {
      this.ejecutarActualizacion(this.carritoId()!, producto);
      return;
    }

    // Si no, intentamos obtenerlo o crearlo
    this.carritoService.obtenerPorUsuario(clienteId).subscribe({
      next: (c) => {
        this.carritoId.set(c.id);
        this.ejecutarActualizacion(c.id, producto);
      },
      error: () => {
        // Si falla (probablemente porque no existe), creamos uno nuevo
        this.carritoService.crear({
          clienteId,
          empresaId: this.empresa()!.id!,
          detalles: [{ productoId: producto.id, cantidad: 1 }]
        }).subscribe({
          next: (c) => this.procesarResultadoCarrito(c, producto.id),
          error: () => {
            this.loadingCarrito.set(false);
            this.addingProductId.set(null);
          }
        });
      }
    });
  }

  private ejecutarActualizacion(cartId: number, producto: ProductoOutputDto) {
    const cantidadActual = this.cantidadesEnCarrito().get(producto.id) || 0;
    const nuevaCantidad = cantidadActual + 1;

    if (nuevaCantidad > producto.cantidad) {
      this.loadingCarrito.set(false);
      this.addingProductId.set(null);
      // Opcional: mostrar un mensaje de error
      alert(`No hay más stock disponible de ${producto.nombre}`);
      return;
    }

    this.carritoService.actualizarCantidadProducto(cartId, producto.id, nuevaCantidad).subscribe({
      next: (c) => this.procesarResultadoCarrito(c, producto.id),
      error: () => {
        this.loadingCarrito.set(false);
        this.addingProductId.set(null);
      }
    });
  }

  private procesarResultadoCarrito(c: CarritoOutputDto, productoId: number) {
    this.carritoId.set(c.id);
    this.addingProductId.set(null);
    this.loadingCarrito.set(false);
    this.addedProductId.set(productoId);
    setTimeout(() => this.addedProductId.set(null), 1500);
  }



  openCart() {
    this.cartOpen.set(true);
  }

  closeCart() {
    this.cartOpen.set(false);
    this.cargarCarritoUsuario();
  }

  volver() {
    this.router.navigate(['/restaurantes']);
  }
}
