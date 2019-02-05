import { TestBed, inject } from '@angular/core/testing';

import { ClientHttpService } from './client-http.service';

describe('ClientHttpService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientHttpService]
    });
  });

  it('should be created', inject([ClientHttpService], (service: ClientHttpService) => {
    expect(service).toBeTruthy();
  }));
});
