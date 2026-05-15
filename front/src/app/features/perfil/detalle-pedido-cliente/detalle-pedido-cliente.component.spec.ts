import { TestBed } from '@angular/core/testing';
import { DetallePedidoClienteComponent } from './detalle-pedido-cliente.component';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { IonContent, IonIcon } from '@ionic/angular/standalone';

describe('DetallePedidoClienteComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallePedidoClienteComponent, IonContent, IonIcon],
      providers: [
        provideHttpClient(),
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => '1' } }
          }
        }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(DetallePedidoClienteComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});