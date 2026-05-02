import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { DetallePedidoService } from './detalle-pedido-service';

describe('DetallePedidoService', () => {
  let service: DetallePedidoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(DetallePedidoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
