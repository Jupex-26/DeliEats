import { TestBed } from '@angular/core/testing';
import { MisPedidosComponent } from './mis-pedidos.component';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { IonIcon } from '@ionic/angular/standalone';

describe('MisPedidosComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisPedidosComponent, IonIcon],
      providers: [
        provideHttpClient(),
        provideRouter([]),
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(MisPedidosComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
