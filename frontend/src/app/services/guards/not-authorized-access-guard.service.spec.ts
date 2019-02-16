import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { NotAuthorizedAccessGuard } from './not-authorized-access-guard.service';

describe('NotAuthorizedAccessGuardService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule,
      RouterTestingModule
    ]
  }));

  it('should be created', () => {
    const service: NotAuthorizedAccessGuard = TestBed.get(NotAuthorizedAccessGuard);
    expect(service).toBeTruthy();
  });
});
