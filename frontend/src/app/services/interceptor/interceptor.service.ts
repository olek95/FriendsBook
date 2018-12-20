import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpSentEvent, HttpHeaderResponse, HttpProgressEvent, HttpResponse, HttpUserEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler):
         Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
        const authorization = request.headers.get('Authorization') || localStorage.getItem('token');
        return next.handle(request.clone({
            url: "http://localhost:8080/FriendsBook" + request.url,
            headers: authorization ? request.headers.set('Authorization', authorization) : request.headers
        }));
    }
}
