import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { DetalleCarritoService } from './detalle-carrito-service';

describe('DetalleCarritoService', () => {
  let service: DetalleCarritoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(DetalleCarritoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});