import { Injectable, signal, WritableSignal, inject, NgZone } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import * as SockJS_ from 'sockjs-client';
const SockJS = (SockJS_ as any).default || SockJS_;
import { Subject, Observable, ReplaySubject, BehaviorSubject, filter, take, switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UbicacionPayload {
  repartidorId: number;
  clienteId: number;
  pedidoId: number;
  latitud: number;
  longitud: number;
}

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private stompClient!: Client;
  private ngZone = inject(NgZone);
  
  private connected$ = new BehaviorSubject<boolean>(false);

  private mensajeSubject = new Subject<any>();
  public mensajeObservable = this.mensajeSubject.asObservable();
  private mensajesStore = new Map<number, WritableSignal<any[]>>();

  private ubicacionSubject = new ReplaySubject<UbicacionPayload>(1);
  public ubicacionObservable = this.ubicacionSubject.asObservable();

  constructor() {}

  getMensajesPedido(pedidoId: number): WritableSignal<any[]> {
    if (!this.mensajesStore.has(pedidoId)) {
      this.mensajesStore.set(pedidoId, signal<any[]>([]));
    }
    return this.mensajesStore.get(pedidoId)!;
  }

  conectar(miUsuarioId?: number) {
    if (this.stompClient && this.stompClient.active) {
      console.log('WebSocket ya está activo o conectando...');
      return;
    }

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${environment.apiUrl}/ws-chat`),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {
      console.log('Conectado a WebSocket STOMP');
      this.connected$.next(true);
      
      if (miUsuarioId) {
        this.stompClient.subscribe(`/topic/mensajes/${miUsuarioId}`, (mensaje) => {
          if (mensaje.body) {
            const msg = JSON.parse(mensaje.body);
            this.ngZone.run(() => {
              this.mensajeSubject.next(msg);
              if (msg.pedidoId) {
                const store = this.getMensajesPedido(msg.pedidoId);
                store.update(prev => [...prev, msg]);
              }
            });
          }
        });
      }
    };

    this.stompClient.onDisconnect = () => {
      this.connected$.next(false);
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
      this.connected$.next(false);
    };

    this.stompClient.activate();
  }

  enviarUbicacion(payload: UbicacionPayload) {
    if (this.stompClient && this.stompClient.connected) {
      console.log('[WebSocketService] Enviando ubicación al destino /app/location:', payload);
      this.stompClient.publish({
        destination: '/app/location',
        body: JSON.stringify(payload),
      });
    } else {
      console.warn('[WebSocketService] No se pudo enviar ubicación: STOMP client no conectado');
    }
  }

  suscribirseAUbicacionCliente(clienteId: number): Observable<UbicacionPayload> {
    
    this.connected$.pipe(
      filter(connected => connected === true),
      take(1)
    ).subscribe(() => {
      console.log(`[WebSocketService] Conectado. Suscribiéndose ahora al topic de ubicación para cliente: ${clienteId}`);
      this.stompClient.subscribe(`/topic/location/${clienteId}`, (mensaje) => {
        if (mensaje.body) {
          const data = JSON.parse(mensaje.body);
          console.log('[WebSocketService] Datos de ubicación recibidos del servidor:', data);
          this.ngZone.run(() => this.ubicacionSubject.next(data));
        }
      });
    });

    return this.ubicacionObservable;
  }

  enviarMensaje(destinoId: number, mensaje: any) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: `/app/chat/${destinoId}`,
        body: JSON.stringify(mensaje),
      });
    } else {
      console.error('No se pudo enviar el mensaje: STOMP client no conectado');
    }
  }

  desconectar() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      console.log('Desconectado de WebSocket STOMP');
    }
  }
}
