import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChatModalComponent } from './chat-modal.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { WebSocketService } from '../../services/websocket/websocket-service';
import { AuthService } from '../../services/auth/auth-service';

describe('ChatModalComponent', () => {
  let component: ChatModalComponent;
  let fixture: ComponentFixture<ChatModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ChatModalComponent,
        HttpClientTestingModule
      ],
      providers: [
        WebSocketService,
        AuthService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ChatModalComponent);
    component = fixture.componentInstance;
    
    // Asignar inputs obligatorios antes de la detección de cambios inicial
    component.pedidoId = 1;
    component.receptorId = 2;
    component.receptorNombre = 'Test User';
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
