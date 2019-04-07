import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { AuthorizationService } from '../authorization/authorization.service';

@Injectable({
  providedIn: 'root'
})
export class NotAuthorizedAccessGuard implements CanActivate {

  limitLength: number;

  constructor(private authorizationService: AuthorizationService, private location: Location, private router: Router) { }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const signed = this.authorizationService.isSigned();
    if (!this.limitLength) {
      this.limitLength = window.history.length;
    }
    if (signed) {
      window.history.length > this.limitLength ? this.location.back() : this.router.navigate(['/home']);
    }
    return !signed;
  }
}
