import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpSentEvent, HttpHeaderResponse, HttpProgressEvent, HttpResponse, HttpUserEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
    constructor() { }
    
    intercept(req: HttpRequest<any>, next: HttpHandler):
         Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
        const baseUrl = "http://localhost:8080/FriendsBook";
        return next.handle(req.clone({
            url: baseUrl + req.url
        }));
    }
}
