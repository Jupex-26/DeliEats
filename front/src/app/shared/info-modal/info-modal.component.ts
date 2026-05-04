import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonModal, IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonButtons, IonList, IonItem, IonLabel,
  IonIcon
} from '@ionic/angular/standalone';
import { CustomError } from '../../types';

@Component({
  selector: 'app-info-modal',
  standalone: true,
  imports: [
    CommonModule,
    IonModal,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButton,
    IonButtons,
    IonList,
    IonItem,
    IonLabel,
    IonIcon,
  ],
  templateUrl: './info-modal.component.html',
  styleUrls: ['./info-modal.component.scss'],
})
export class InfoModalComponent {
  @Input() isOpen = false;
  @Input() title = 'Información';
  @Input() message = '';
  @Input() errorData: CustomError | null = null;
  @Input() type: 'success' | 'error' | 'info' = 'info';

  @Output() modalClosed = new EventEmitter<void>();

  closeModal() {
    this.isOpen = false;
    this.modalClosed.emit();
  }

  hasFieldErrors(): boolean {
    return !!this.errorData?.fieldErrors && Object.keys(this.errorData.fieldErrors).length > 0;
  }

  getFieldErrorsList(): { field: string; message: string }[] {
    if (!this.hasFieldErrors()) return [];

    return Object.entries(this.errorData!.fieldErrors!).map(([key, value]) => ({
      field: key,
      message: value,
    }));
  }
}
