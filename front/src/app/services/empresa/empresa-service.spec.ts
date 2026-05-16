import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { EmpresaService } from './empresa-service';

describe('EmpresaService', () => {
  let service: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(EmpresaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});