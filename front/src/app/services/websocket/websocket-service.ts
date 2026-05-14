import { Injectable, signal, WritableSignal, inject, NgZone } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS_ from 'sockjs-client';
const SockJS = (SockJS_ as any).default || SockJS_;
import { Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private stompClient!: Client;
  private ngZone = inject(NgZone);
  private mensajeSubject = new Subject<any>();
  public mensajeObservable = this.mensajeSubject.asObservable();

  // Store de mensajes por pedidoId para persistencia durante la sesión
  private mensajesStore = new Map<number, WritableSignal<any[]>>();

  constructor() {}

  getMensajesPedido(pedidoId: number): WritableSignal<any[]> {
    if (!this.mensajesStore.has(pedidoId)) {
      this.mensajesStore.set(pedidoId, signal<any[]>([]));
    }
    return this.mensajesStore.get(pedidoId)!;
  }

  conectar(miUsuarioId: number) {
    this.stompClient = new Client({
      // Usamos el environment para la URL base
      webSocketFactory: () => new SockJS(`${environment.apiUrl}/ws-chat`),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {
      console.log('Conectado a WebSocket STOMP');
      // Suscribirse al canal personal del usuario (el receptor)
      this.stompClient.subscribe(`/topic/mensajes/${miUsuarioId}`, (mensaje) => {
        if (mensaje.body) {
          const msg = JSON.parse(mensaje.body);
          this.ngZone.run(() => {
            // Notificar a través del Subject para que el componente lo reciba
            this.mensajeSubject.next(msg);
            
            // Si el mensaje trae pedidoId lo guardamos en su store específico
            // Si no lo trae, el componente se encargará de filtrarlo por emisor/receptor
            if (msg.pedidoId) {
              const store = this.getMensajesPedido(msg.pedidoId);
              store.update(prev => [...prev, msg]);
            }
          });
        }
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.stompClient.activate();
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
