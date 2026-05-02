import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { EstadoService } from './estado-service';

describe('EstadoService', () => {
  let service: EstadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(EstadoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
