import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { CarritoService } from './carrito-service';

describe('CarritoService', () => {
  let service: CarritoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(CarritoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});