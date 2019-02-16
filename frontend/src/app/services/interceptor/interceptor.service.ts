import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpSentEvent, HttpHeaderResponse, HttpProgressEvent, HttpResponse, HttpUserEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthorizationService } from '../authorization/authorization.service';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
    constructor(private authService: AuthorizationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler):
         Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
        const authenticationDetails = this.authService.getAuthenticationDetails();
        const authorization = request.headers.get('Authorization') || (authenticationDetails ? authenticationDetails.token : null);
        return next.handle(request.clone({
            url: 'http://localhost:8080/FriendsBook' + request.url,
            headers: authorization ? request.headers.set('Authorization', authorization) : request.headers
        }));
    }
}
