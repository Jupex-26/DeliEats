import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AperturaService } from './apertura-service';

describe('AperturaService', () => {
  let service: AperturaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AperturaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});