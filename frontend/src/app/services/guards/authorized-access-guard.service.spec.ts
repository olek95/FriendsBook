import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AuthorizedAccessGuard } from './authorized-access-guard.service';

describe('AuthorizedAccessGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule,
      RouterTestingModule
    ]
  }));

  it('should be created', () => {
    const service: AuthorizedAccessGuard = TestBed.get(AuthorizedAccessGuard);
    expect(service).toBeTruthy();
  });
});
