import { TestBed } from '@angular/core/testing';

import { ClientHttpService } from './client-http.service';

describe('ClientHttpService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ClientHttpService = TestBed.get(ClientHttpService);
    expect(service).toBeTruthy();
  });
});
