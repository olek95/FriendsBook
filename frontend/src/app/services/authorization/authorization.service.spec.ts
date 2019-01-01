import { TestBed, inject } from '@angular/core/testing';

import { AuthorizationService } from './authorization.service';
import { HttpClientModule } from '@angular/common/http';

describe('AuthorizationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [AuthorizationService]
    });
  });

  it('should be created', inject([AuthorizationService], (service: AuthorizationService) => {
    expect(service).toBeTruthy();
  }));
});
