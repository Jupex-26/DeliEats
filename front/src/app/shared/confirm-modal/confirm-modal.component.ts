import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonModal, IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/angular/standalone';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule, IonModal, IonHeader, IonToolbar, IonTitle, IonContent],
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.scss']
})
export class ConfirmModalComponent {
  @Input() isOpen = false;
  @Input() title = 'Confirmar Acción';
  @Input() message = '¿Estás seguro de que deseas realizar esta acción?';
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
  @Input() dangerAction = false; // Si es true, el botón de confirmar será rojo

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onCancel() {
    this.isOpen = false;
    this.cancel.emit();
  }

  onConfirm() {
    this.isOpen = false;
    this.confirm.emit();
  }
}
