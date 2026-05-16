import { Injectable, inject, signal } from '@angular/core';
import { Observable, Subject, Subscription, ReplaySubject } from 'rxjs';
import { WebSocketService, UbicacionPayload } from '../websocket/websocket-service';

export interface UbicacionRepartidor {
  lat: number;
  lng: number;
}

@Injectable({
  providedIn: 'root'
})
export class TrackingService {
  private wsService = inject(WebSocketService);
  private locationSub?: Subscription;
  private watchId: number | null = null;

  private ubicacion = signal<UbicacionRepartidor | null>(null);
  readonly ubicacionActual = this.ubicacion.asReadonly();

  private ubicacionSubject = new ReplaySubject<UbicacionRepartidor>(1);
  ubicacion$ = this.ubicacionSubject.asObservable();

  conectar(clienteId: number): void {
    console.log(`[TrackingService] Conectando al canal de ubicación del cliente ${clienteId}...`);
    this.wsService.conectar();

    this.locationSub = this.wsService
      .suscribirseAUbicacionCliente(clienteId)
      .subscribe({
        next: (payload: UbicacionPayload) => {
          console.log('[TrackingService] Recibido payload de WS:', payload);
          // Mapeamos latitud/longitud a lat/lng para el mapa Leaflet
          const coords = { lat: payload.latitud, lng: payload.longitud };
          this.ubicacion.set(coords);
          this.ubicacionSubject.next(coords);
        },
        error: (err) => console.error('[TrackingService] Error en suscripción:', err)
      });
  }

  iniciarRastreo(repartidorId: number, clienteId: number, pedidoId: number) {
    if (!navigator.geolocation) {
      console.error('La geolocalización no es soportada por este navegador.');
      return;
    }

    this.wsService.conectar();

    const options: PositionOptions = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0
    };

    if (this.watchId !== null) {
      clearInterval(this.watchId);
    }

    this.watchId = window.setInterval(() => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const payload: UbicacionPayload = {
            repartidorId,
            clienteId,
            pedidoId,
            latitud: position.coords.latitude,
            longitud: position.coords.longitude
          };

          console.log('[TrackingService] Enviando ubicación cada 1s:', payload);
          this.wsService.enviarUbicacion(payload);
        },
        (error) => console.error('[TrackingService] Error al obtener ubicación:', error),
        options
      );
    }, 1000) as any;
  }

  desconectar(): void {
    if (this.watchId !== null) {
      clearInterval(this.watchId);
      this.watchId = null;
      console.log('[TrackingService] Rastreo (emisor) detenido.');
    }

    if (this.locationSub) {
      this.locationSub.unsubscribe();
      console.log('[TrackingService] Suscripción (receptor) detenida.');
    }

    this.ubicacion.set(null);
  }
}
