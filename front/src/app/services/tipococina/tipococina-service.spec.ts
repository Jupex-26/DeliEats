import { TestBed } from '@angular/core/testing';

import { TipococinaService } from './tipococina-service';

describe('TipococinaService', () => {
  let service: TipococinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipococinaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
