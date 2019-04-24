import { TestBed, inject } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { NotAuthorizedAccessGuard } from './not-authorized-access-guard.service';
import { AuthorizationService } from '../authorization/authorization.service';

describe('NotAuthorizedAccessGuardService', () => {
  let guard: NotAuthorizedAccessGuard;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule,
      RouterTestingModule
    ]
  }));

  beforeEach(inject([ NotAuthorizedAccessGuard ], (notAuthorizedAccessGuard) => {
    guard = notAuthorizedAccessGuard;
  }));

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should return true when user is not signed', () => {
    localStorage.removeItem('authenticationDetails');
    expect(guard.canActivate()).toBeTruthy();
  });

  it('should navigate to home when user is logged', inject([ AuthorizationService, Router ], (authorizationService, router) => {
    authorizationService.saveAuthenticationDetails('token', 'login');
    spyOn(router, 'navigate');
    expect(guard.canActivate()).toBeFalsy();
    expect(router.navigate).toHaveBeenCalledWith(['/home']);
  }));
});
