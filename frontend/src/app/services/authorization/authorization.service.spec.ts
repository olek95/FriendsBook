import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AuthorizationService } from './authorization.service';

describe('AuthorizationService', () => {
  let service: AuthorizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ AuthorizationService ]
    });
  });

  beforeEach(inject([ AuthorizationService ], (authorizationService) => {
    service = authorizationService;
  }));

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save authorization data', () => {
    const authenticationDetails = {
      login: 'login',
      token: 'token'
    };
    localStorage.removeItem('authenticationDetails');
    service.saveAuthenticationDetails(authenticationDetails.token, authenticationDetails.login);
    expect(service.getAuthenticationDetails()).toEqual(authenticationDetails);
  });

  it('should detect that user is signed', () => {
    localStorage.removeItem('authenticationDetails');
    service.saveAuthenticationDetails('login', 'token');
    expect(service.isSigned()).toBeTruthy();
  });

  it('should detect that user is not signed', () => {
    localStorage.removeItem('authenticationDetails');
    expect(service.isSigned()).toBeFalsy();
  });
});
