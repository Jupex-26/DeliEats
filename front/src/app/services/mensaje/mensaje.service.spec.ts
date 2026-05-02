import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MensajeService } from './mensaje.service';

describe('MensajeService', () => {
  let service: MensajeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(MensajeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
