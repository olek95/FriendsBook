import { async, TestBed, inject } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { InterceptorService } from './interceptor.service';
import { AuthorizationService } from '../authorization/authorization.service';

describe('InterceptorService', () => {
  let authorizationService: AuthorizationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: InterceptorService,
          multi: true
        }
      ]
    })
  }));

  beforeEach(inject([AuthorizationService], (authorizationService) => {
    this.authorizationService = authorizationService;
  }));

  it('should be created', () => {
    const service: InterceptorService = TestBed.get(InterceptorService);
    expect(service).toBeTruthy();
  });

  it('each url has correct prefix', inject([HttpClient, HttpTestingController], (http: HttpClient, mock: HttpTestingController) => {
    http.get('/urlExample').subscribe(response => {});
    mock.expectOne(req => req.url.startsWith('http://localhost:8080/FriendsBook'));
    expect(true).toBeTruthy();
  }));

  it('request should not to have authorization token when it is not saved in localstorage', inject([HttpClient, HttpTestingController],
    (http: HttpClient, mock: HttpTestingController) => {
    localStorage.removeItem('authenticationDetails');
    http.get('/urlExample').subscribe(response => {});
    mock.expectOne(req => !req.headers.has('Authorization'));
    expect(true).toBeTruthy();
  }));

  it('request should to have authorization token when it is saved in localstorage', inject([HttpClient, HttpTestingController],
    (http: HttpClient, mock: HttpTestingController) => {
    this.authorizationService.saveAuthenticationDetails('token', 0);
    http.get('/urlExample').subscribe(response => {});
    mock.expectOne(req => req.headers.get('Authorization') === 'token');
    localStorage.removeItem('authenticationDetails');
    expect(true).toBeTruthy();
  }));

  afterEach(inject([HttpTestingController], (mock: HttpTestingController) => {
    mock.verify();
  }));
});
