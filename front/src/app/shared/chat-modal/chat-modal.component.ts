import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, inject, signal, ViewChild, ElementRef, AfterViewChecked, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonModal, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { sendOutline, closeOutline, personCircleOutline } from 'ionicons/icons';
import { WebSocketService } from '../../services/websocket/websocket-service';
import { AuthService } from '../../services/auth/auth-service';
import { Subscription } from 'rxjs';

export interface MensajeChat {
  pedidoId?: number;
  emisorId: number;
  receptorId: number;
  contenido: string;
  fechaEnvio: any;
}

@Component({
  selector: 'app-chat-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, IonModal, IonIcon],
  templateUrl: './chat-modal.component.html',
  styleUrls: ['./chat-modal.component.scss'],
})
export class ChatModalComponent implements OnInit, OnDestroy, AfterViewChecked {
  @Input() isOpen = false;
  @Input() pedidoId!: number;
  @Input() receptorId!: number;
  @Input() receptorNombre = 'Usuario';
  @Output() closed = new EventEmitter<void>();

  @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

  private webSocketService = inject(WebSocketService);
  private authService = inject(AuthService);
  private ngZone = inject(NgZone);

  mensajes = signal<MensajeChat[]>([]);
  nuevoMensaje = '';
  miUsuarioId!: number;
  private subscription!: Subscription;

  constructor() {
    addIcons({ sendOutline, closeOutline, personCircleOutline });
  }

  ngOnInit() {
    const user = this.authService.currentUser();
    if (user?.userOutputDto?.id) {
      this.miUsuarioId = user.userOutputDto.id;
      this.webSocketService.conectar(this.miUsuarioId);

      // Vincular con el store persistente del servicio
      this.mensajes = this.webSocketService.getMensajesPedido(this.pedidoId);

      this.subscription = this.webSocketService.mensajeObservable.subscribe((msg) => {
        this.ngZone.run(() => {
          // Si el mensaje viene del receptor actual (o de mí mismo en el broadcast)
          if (msg.emisorId === this.receptorId || msg.emisorId === this.miUsuarioId) {
            // Evitar duplicados (por si ya lo añadimos localmente en enviar())
            const existe = this.mensajes().some(m => 
              m.contenido === msg.contenido && 
              m.emisorId === msg.emisorId &&
              (m.fechaEnvio === msg.fechaEnvio || (m as any).id === (msg as any).id)
            );
            
            if (!existe) {
              this.mensajes.update((prev) => [...prev, msg]);
            }
          }
        });
      });
    }
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.webSocketService.desconectar();
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) { }
  }

  enviar() {
    if (!this.nuevoMensaje.trim()) return;

    const mensaje: MensajeChat = {
      pedidoId: this.pedidoId,
      emisorId: this.miUsuarioId,
      receptorId: this.receptorId,
      contenido: this.nuevoMensaje,
      fechaEnvio: new Date().toISOString(),
    };

    this.webSocketService.enviarMensaje(this.receptorId, mensaje);

    // Lo añadimos al store centralizado para feedback inmediato
    this.mensajes.update((prev) => [...prev, mensaje]);
    this.nuevoMensaje = '';
  }

  close() {
    this.isOpen = false;
    this.closed.emit();
  }
}
