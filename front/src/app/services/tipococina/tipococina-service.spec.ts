import { TestBed } from '@angular/core/testing';

import { TipoCocinaService } from './tipococina-service';

describe('TipoCocinaService', () => {
  let service: TipoCocinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoCocinaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});