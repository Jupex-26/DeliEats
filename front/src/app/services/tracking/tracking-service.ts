import { Injectable, signal } from '@angular/core';
import { Observable, Subject } from 'rxjs';

export interface UbicacionRepartidor {
  lat: number;
  lng: number;
}

/**
 * TrackingService — Stub para tracking en tiempo real vía Kafka
 *
 * TODO: Cuando el backend de Kafka/WebSocket esté definido, sustituir
 * la simulación por una conexión real.
 * Protocolo esperado: WebSocket/STOMP sobre SockJS
 * Topic esperado: /topic/pedido/{pedidoId}/ubicacion
 * Mensaje: { lat: number, lng: number }
 *
 * Instalar dependencias cuando sea necesario:
 *   npm install @stomp/stompjs sockjs-client
 *   npm install --save-dev @types/sockjs-client
 */
@Injectable({
  providedIn: 'root'
})
export class TrackingService {

  private ubicacion = signal<UbicacionRepartidor | null>(null);
  readonly ubicacionActual = this.ubicacion.asReadonly();

  // Subject para emitir actualizaciones
  private ubicacionSubject = new Subject<UbicacionRepartidor>();
  ubicacion$ = this.ubicacionSubject.asObservable();

  /**
   * Conecta al canal de tracking de un pedido.
   * TODO: Reemplazar con conexión STOMP/WebSocket real cuando el backend esté listo.
   */
  conectar(pedidoId: number): void {
    console.warn(`[TrackingService] Conectando al canal del pedido ${pedidoId}... (TODO: conectar Kafka/WebSocket)`);
    // Ejemplo de conexión STOMP (descomentar cuando el backend esté listo):
    // const client = new Client({
    //   webSocketFactory: () => new SockJS(`${environment.wsUrl}/ws`),
    //   onConnect: () => {
    //     client.subscribe(`/topic/pedido/${pedidoId}/ubicacion`, (msg) => {
    //       const ubicacion: UbicacionRepartidor = JSON.parse(msg.body);
    //       this.ubicacion.set(ubicacion);
    //       this.ubicacionSubject.next(ubicacion);
    //     });
    //   }
    // });
    // client.activate();
  }

  /**
   * Desconecta del canal de tracking.
   */
  desconectar(): void {
    console.warn('[TrackingService] Desconectando... (TODO: cerrar WebSocket)');
    this.ubicacion.set(null);
  }
}
