import { TestBed, inject } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { AuthorizedAccessGuard } from './authorized-access-guard.service';
import { AuthorizationService } from '../authorization/authorization.service';

describe('AuthorizedAccessGuardService', () => {
  let guard: AuthorizedAccessGuard;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule,
      RouterTestingModule
    ]
  }));

  beforeEach(inject([ AuthorizedAccessGuard ], (authorizedAccessGuard) => {
      guard = authorizedAccessGuard;
  }));

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should return true if user is signed', inject([ AuthorizationService ], (authorizationService) => {
    authorizationService.saveAuthenticationDetails('token', 'login');
    expect(guard.canActivate()).toBeTruthy();
  }));

  it('should navigate user is unsigned', inject([ Router ], (router: Router) => {
    localStorage.removeItem('authenticationDetails');
    spyOn(router, 'navigate');
    expect(guard.canActivate()).toBeFalsy();
    expect(router.navigate).toHaveBeenCalledWith([""]);
  }));
});
