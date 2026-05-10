import {
  Component, OnInit, OnDestroy,
  AfterViewInit, inject, signal, ElementRef, ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  arrowBackOutline,
  personOutline,
  locationOutline,
  closeCircleOutline,
  bicycleOutline,
  timeOutline,
  refreshOutline,
  checkmarkCircleOutline,
  downloadOutline,
  alertCircleOutline,
  receiptOutline
} from 'ionicons/icons';
import { Subscription } from 'rxjs';
import * as L from 'leaflet';
import { PedidoService } from '../../../services/pedido/pedido-service';
import { TrackingService } from '../../../services/tracking/tracking-service';
import { PedidoOutputDto } from '../../../types';
import { EuroPipe } from '../../../pipe/euro.pipe';

// Fix Leaflet default icon paths for Angular bundling
const iconDefault = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

const repartidorIcon = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

@Component({
  selector: 'app-detalle-pedido-cliente',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, EuroPipe],
  templateUrl: './detalle-pedido-cliente.component.html',
  styleUrls: ['./detalle-pedido-cliente.component.scss']
})
export class DetallePedidoClienteComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('mapContainer') mapContainer!: ElementRef;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private pedidoService = inject(PedidoService);
  private trackingService = inject(TrackingService);

  pedido = signal<PedidoOutputDto | null>(null);
  loading = signal(true);
  cancelando = signal(false);
  descargando = signal(false);
  errorCancelar = signal<string | null>(null);
  exitoCancelar = signal(false);

  private map?: L.Map;
  private repartidorMarker?: L.Marker;
  private trackingSub?: Subscription;

  constructor() {
    addIcons({
      arrowBackOutline, personOutline, locationOutline,
      closeCircleOutline, bicycleOutline, timeOutline,
      refreshOutline, checkmarkCircleOutline, downloadOutline,
      alertCircleOutline, receiptOutline
    });
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.router.navigate(['/perfil']); return; }

    this.pedidoService.obtenerPorId(id).subscribe({
      next: (p) => {
        this.pedido.set(p);
        this.loading.set(false);
        if (p.estadoNombre === 'EN CAMINO') {
          this.trackingService.conectar(id);
          // Wait one tick for the @if block to render the #mapContainer in the DOM
          setTimeout(() => this.initMap(), 100);
        }
      },
      error: () => {
        this.loading.set(false);
        this.router.navigate(['/perfil']);
      }
    });
  }

  ngAfterViewInit() {
    // Map init is triggered from ngOnInit via setTimeout after data loads
  }

  private initMap(lat = 40.4168, lng = -3.7038) {
    if (this.map || !this.mapContainer) return;

    this.map = L.map(this.mapContainer.nativeElement).setView([lat, lng], 14);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      maxZoom: 19
    }).addTo(this.map);

    // Suscribirse a actualizaciones de ubicación del repartidor
    this.trackingSub = this.trackingService.ubicacion$.subscribe(({ lat, lng }) => {
      if (!this.map) return;
      if (this.repartidorMarker) {
        this.repartidorMarker.setLatLng([lat, lng]);
      } else {
        this.repartidorMarker = L.marker([lat, lng], { icon: repartidorIcon })
          .addTo(this.map!)
          .bindPopup('🛵 Repartidor en camino');
      }
      this.map.setView([lat, lng], 15);
    });
  }

  ngOnDestroy() {
    this.trackingSub?.unsubscribe();
    this.trackingService.desconectar();
    this.map?.remove();
  }

  cancelarPedido() {
    const p = this.pedido();
    if (!p || this.cancelando() || p.estadoNombre === 'EN CAMINO') return;

    this.cancelando.set(true);
    this.errorCancelar.set(null);

    this.pedidoService.cancelar(p.id).subscribe({
      next: (updated) => {
        this.pedido.set(updated);
        this.cancelando.set(false);
        this.exitoCancelar.set(true);
      },
      error: () => {
        this.cancelando.set(false);
        this.errorCancelar.set('No se pudo cancelar el pedido. Inténtalo de nuevo.');
      }
    });
  }

  descargarFactura() {
    const p = this.pedido();
    if (!p || this.descargando()) return;

    this.descargando.set(true);
    this.pedidoService.descargarFactura(p.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `factura-pedido-${p.id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.descargando.set(false);
      },
      error: () => this.descargando.set(false)
    });
  }

  get puedeCancel(): boolean {
    const estado = this.pedido()?.estadoNombre;
    return !!estado && estado !== 'EN CAMINO' && estado !== 'ENTREGADO' && estado !== 'CANCELADO';
  }

  get mostrarMapa(): boolean {
    return this.pedido()?.estadoNombre === 'EN CAMINO';
  }

  get nombreRepartidor(): string {
    const p = this.pedido();
    if (!p) return '';
    if (p.estadoNombre === 'EN CAMINO') {
      return p.nombreRepartidor ?? 'Por definir';
    }
    return p.nombreRepartidor ?? 'Por definir';
  }

  getEstadoIcono(estado: string): string {
    const map: Record<string, string> = {
      'PENDIENTE': 'time-outline',
      'PREPARANDO': 'refresh-outline',
      'EN CAMINO': 'bicycle-outline',
      'ENTREGADO': 'checkmark-circle-outline',
      'CANCELADO': 'close-circle-outline'
    };
    return map[estado] ?? 'receipt-outline';
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-ES', {
      day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit'
    });
  }

  volver() {
    this.router.navigate(['/perfil']);
  }
}
