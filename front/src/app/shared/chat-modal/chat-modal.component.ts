import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, inject, signal, ViewChild, ElementRef, AfterViewChecked, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonModal, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { sendOutline, closeOutline, personCircleOutline } from 'ionicons/icons';
import { WebSocketService } from '../../services/websocket/websocket-service';
import { AuthService } from '../../services/auth/auth-service';
import { Subscription } from 'rxjs';
import { MensajeService } from '../../services/mensaje/mensaje-service';

export interface MensajeChat {
  id?: number;
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
  private mensajeService = inject(MensajeService);
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

      this.mensajes = this.webSocketService.getMensajesPedido(this.pedidoId);

      this.cargarHistorial();

      this.subscription = this.webSocketService.mensajeObservable.subscribe((msg) => {
        this.ngZone.run(() => {
          
          if (msg.emisorId === this.receptorId || msg.emisorId === this.miUsuarioId) {
            
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

    this.mensajes.update((prev) => [...prev, mensaje]);
    this.nuevoMensaje = '';
  }

  close() {
    this.isOpen = false;
    this.closed.emit();
  }

  private cargarHistorial() {
    this.mensajeService.obtenerChat(this.miUsuarioId, this.receptorId).subscribe({
      next: (res) => {
        const historial = res.content || res; 
        if (Array.isArray(historial)) {
          
          this.mensajes.update(actuales => {
            const idsActuales = new Set(actuales.map(m => m.id).filter(id => id !== undefined));
            const nuevos = historial.filter(m => !idsActuales.has(m.id));
            
            const total = [...actuales, ...nuevos];
            return total.sort((a, b) => new Date(a.fechaEnvio).getTime() - new Date(b.fechaEnvio).getTime());
          });
        }
      },
      error: (err) => console.error('Error cargando historial de chat:', err)
    });
  }
}