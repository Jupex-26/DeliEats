import {
  Component, OnInit, OnDestroy,
  AfterViewInit, inject, signal, ElementRef, ViewChild, computed, effect
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
  receiptOutline,
  chatbubblesOutline
} from 'ionicons/icons';
import { Subscription } from 'rxjs';
import * as L from 'leaflet';
import { PedidoService } from '../../../services/pedido/pedido-service';
import { TrackingService } from '../../../services/tracking/tracking-service';
import { PedidoOutputDto, RepartidorOutputDto } from '../../../types';
import { EuroPipe } from '../../../pipe/euro.pipe';
import { ChatModalComponent } from '../../../shared/chat-modal/chat-modal.component';
import { environment } from '../../../../environments/environment';
import { RepartidorService } from '../../../services/repartidor/repartidor-service';

const iconDefault = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

const repartidorIcon = L.divIcon({
  className: 'custom-repartidor-icon',
  html: `
    <div class="moto-marker-pulse">
      <div class="moto-icon">🛵</div>
    </div>
  `,
  iconSize: [40, 40],
  iconAnchor: [20, 20]
});

const clienteIcon = L.divIcon({
  className: 'custom-cliente-icon',
  html: `
    <div class="house-marker">
      <div class="house-icon">🏠</div>
    </div>
  `,
  iconSize: [40, 40],
  iconAnchor: [20, 20]
});

@Component({
  selector: 'app-detalle-pedido-cliente',
  standalone: true,
  imports: [CommonModule, IonContent, IonIcon, EuroPipe, ChatModalComponent],
  templateUrl: './detalle-pedido-cliente.component.html',
  styleUrls: ['./detalle-pedido-cliente.component.scss']
})
export class DetallePedidoClienteComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('mapContainer') mapContainer!: ElementRef;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private pedidoService = inject(PedidoService);
  private trackingService = inject(TrackingService);
  private repartidorService = inject(RepartidorService);
  protected environment = environment;

  pedido = signal<PedidoOutputDto | null>(null);
  loading = signal(true);
  cancelando = signal(false);
  descargando = signal(false);
  errorCancelar = signal<string | null>(null);
  exitoCancelar = signal(false);

  isChatOpen = signal(false);
  chatReceptorId = signal<number | null>(null);
  chatReceptorNombre = signal<string>('');

  mostrarMapa = computed(() => {
    const estado = this.pedido()?.estadoNombre?.toUpperCase();
    return estado === 'EN CAMINO';
  });

  private map?: L.Map;
  private repartidorMarker?: L.Marker;
  private clienteMarker?: L.Marker;
  private trackingSub?: Subscription;
  private ultimaUbicacionRecibida?: { lat: number, lng: number };

  constructor() {
    addIcons({
      arrowBackOutline, personOutline, locationOutline,
      closeCircleOutline, bicycleOutline, timeOutline,
      refreshOutline, checkmarkCircleOutline, downloadOutline,
      alertCircleOutline, receiptOutline, chatbubblesOutline
    });

    effect(() => {
      if (this.mostrarMapa()) {
        const p = this.pedido();
        if (p) {
          this.trackingService.conectar(p.clienteId);
          // Intentamos inicializar el mapa cuando el DOM esté disponible
          setTimeout(() => this.initMap(), 1000);
        }
      }
    });
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.router.navigate(['/perfil']); return; }

    this.pedidoService.obtenerPorId(id).subscribe({
      next: (p) => {
        this.pedido.set(p);
        this.loading.set(false);

        // Si no tenemos repartidorClienteId pero sí repartidorId, lo buscamos para habilitar el chat
        if (!p.repartidorClienteId && p.repartidorId) {
          this.repartidorService.obtenerPorId(p.repartidorId).subscribe({
            next: (rep) => {
              this.pedido.update(current => {
                if (!current) return null;
                return { ...current, repartidorClienteId: rep.clienteId };
              });
            }
          });
        }
      },
      error: () => {
        this.loading.set(false);
        this.router.navigate(['/perfil']);
      }
    });
  }

  ngAfterViewInit() {
    
  }

  private initMap(lat = 40.4168, lng = -3.7038) {
    console.log('[DetallePedidoCliente] Intentando inicializar mapa...');
    if (this.map) {
      console.log('[DetallePedidoCliente] El mapa ya existe.');
      return;
    }
    if (!this.mapContainer) {
      console.warn('[DetallePedidoCliente] mapContainer no encontrado en el DOM todavía.');
      return;
    }
    
    console.log('[DetallePedidoCliente] Inicializando mapa en:', lat, lng);
    this.map = L.map(this.mapContainer.nativeElement).setView([lat, lng], 14);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      maxZoom: 19
    }).addTo(this.map);

    const direccion = this.pedido()?.direccionEntrega;
    if (direccion) {
      this.geocodificarDireccion(direccion);
    }

    if (this.ultimaUbicacionRecibida) {
      this.actualizarMarcador(this.ultimaUbicacionRecibida.lat, this.ultimaUbicacionRecibida.lng);
    }

    this.trackingSub = this.trackingService.ubicacion$.subscribe(({ lat, lng }) => {
      console.log(`[DetallePedidoCliente] Ubicación recibida del TrackingService: ${lat}, ${lng}`);
      this.ultimaUbicacionRecibida = { lat, lng };
      
      if (this.map) {
        this.actualizarMarcador(lat, lng);
      } else {
        console.warn('[DetallePedidoCliente] El mapa no está inicializado todavía. Guardando ubicación...');
      }
    });
  }

  private actualizarMarcador(lat: number, lng: number) {
    if (!this.map) return;

    if (this.repartidorMarker) {
      this.repartidorMarker.setLatLng([lat, lng]);
    } else {
      console.log('[DetallePedidoCliente] Creando nuevo marcador para el repartidor');
      this.repartidorMarker = L.marker([lat, lng], { icon: repartidorIcon })
        .addTo(this.map)
        .bindPopup('🛵 Repartidor en camino');
    }
    
    this.map.setView([lat, lng], 16);
  }

  private geocodificarDireccion(direccion: string) {
    
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccion)}&limit=1`;
    
    fetch(url)
      .then(res => res.json())
      .then(data => {
        if (data && data.length > 0) {
          const lat = parseFloat(data[0].lat);
          const lon = parseFloat(data[0].lon);
          
          if (this.map) {
            this.clienteMarker = L.marker([lat, lon], { icon: clienteIcon })
              .addTo(this.map)
              .bindPopup(`🏠 Destino: ${direccion}`);
            
            if (!this.repartidorMarker) {
              this.map.setView([lat, lon], 15);
            }
          }
        }
      })
      .catch(err => console.error('Error al geocodificar dirección:', err));
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
    const estado = this.pedido()?.estadoNombre?.toUpperCase();
    return !!estado && estado !== 'EN CAMINO' && estado !== 'ENTREGADO' && estado !== 'CANCELADO';
  }

  // get mostrarMapa ya no es necesario, es un computed signal


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

  abrirChatRepartidor() {
    const p = this.pedido();
    if (p?.repartidorClienteId) {
      this.chatReceptorId.set(p.repartidorClienteId);
      this.chatReceptorNombre.set(p.nombreRepartidor || 'Repartidor');
      this.isChatOpen.set(true);
    }
  }
}