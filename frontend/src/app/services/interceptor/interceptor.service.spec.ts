import { async, TestBed, inject } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { InterceptorService } from './interceptor.service';

describe('InterceptorService', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
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
    http.get('/urlExample').subscribe(response => {});
    mock.expectOne(req => !req.headers.has('Authorization'));
    expect(true).toBeTruthy();
  }));

  it('request should to have authorization token when it is saved in localstorage', inject([HttpClient, HttpTestingController],
    (http: HttpClient, mock: HttpTestingController) => {
    localStorage.setItem('token', 'token');
    http.get('/urlExample').subscribe(response => {});
    mock.expectOne(req => req.headers.get('Authorization') === 'token');
    localStorage.removeItem('token');
    expect(true).toBeTruthy();
  }));

  afterEach(inject([HttpTestingController], (mock: HttpTestingController) => {
    mock.verify();
  }));
});
