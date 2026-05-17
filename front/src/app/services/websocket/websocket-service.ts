import { Injectable, signal, WritableSignal, inject, NgZone } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS_ from 'sockjs-client';
const SockJS = (SockJS_ as any).default || SockJS_;
import { Subject, Observable, ReplaySubject, BehaviorSubject, filter, take } from 'rxjs';
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
  private miUsuarioId: number | null = null;
  private mensajeSubscription: any = null;

  private mensajeSubject = new Subject<any>();
  public mensajeObservable = this.mensajeSubject.asObservable();
  private mensajesStore = new Map<number, WritableSignal<any[]>>();

  private ubicacionSubject = new ReplaySubject<UbicacionPayload>(1);
  private ubicacionObservable = this.ubicacionSubject.asObservable();
  private locationStompSub: any = null; // guardia para evitar suscripciones duplicadas

  conectar(miUsuarioId?: number) {
    if (miUsuarioId) {
      this.miUsuarioId = miUsuarioId;
    }

    if (this.stompClient && this.stompClient.active) {
      if (this.stompClient.connected) {
        this.suscribirAMensajesPrivados();
      }
      return;
    }

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${environment.apiUrl}/ws-chat`),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Conectado a STOMP');
      this.connected$.next(true);
      this.suscribirAMensajesPrivados();
    };

    this.stompClient.onDisconnect = () => {
      console.log('Desconectado de STOMP');
      this.connected$.next(false);
      this.mensajeSubscription = null;
    };

    this.stompClient.activate();
  }

  private suscribirAMensajesPrivados() {
    if (!this.miUsuarioId || !this.stompClient || !this.stompClient.connected) return;
    if (this.mensajeSubscription) return;

    console.log(`Suscribiéndose a /topic/mensajes/${this.miUsuarioId}`);
    this.mensajeSubscription = this.stompClient.subscribe(`/topic/mensajes/${this.miUsuarioId}`, (mensaje) => {
      if (mensaje.body) {
        this.ngZone.run(() => {
          const msg = JSON.parse(mensaje.body);
          this.mensajeSubject.next(msg);
        });
      }
    });
  }

  getMensajesPedido(pedidoId: number): WritableSignal<any[]> {
    if (!this.mensajesStore.has(pedidoId)) {
      this.mensajesStore.set(pedidoId, signal<any[]>([]));
    }
    return this.mensajesStore.get(pedidoId)!;
  }

  suscribirseAUbicacionCliente(clienteId: number): Observable<UbicacionPayload> {
    this.connected$.pipe(
      filter(connected => connected === true),
      take(1)
    ).subscribe(() => {
      // Guardia: no crear suscripción duplicada al mismo topic
      if (this.locationStompSub) return;

      this.locationStompSub = this.stompClient.subscribe(`/topic/location/${clienteId}`, (mensaje) => {
        if (mensaje.body) {
          const data = JSON.parse(mensaje.body);
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

  enviarUbicacion(payload: UbicacionPayload) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/app/location',
        body: JSON.stringify(payload),
      });
    }
  }

  desconectar() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
    this.connected$.next(false);
    this.mensajeSubscription = null;
    this.miUsuarioId = null;
    this.locationStompSub = null; // resetear para permitir nueva suscripción
  }
}
