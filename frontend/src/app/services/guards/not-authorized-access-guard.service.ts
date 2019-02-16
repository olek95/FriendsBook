import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { AuthorizedAccessGuard } from './authorized-access-guard.service';
import { CanActivate, UrlTree } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import {AuthorizationService} from "../authorization/authorization.service";

@Injectable({
  providedIn: 'root'
})
export class NotAuthorizedAccessGuard implements CanActivate {

  constructor(private authorizationService: AuthorizationService, private location: Location) { }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const signed = this.authorizationService.isSigned();
    if (signed) {
      this.location.back();
    }
    return !signed;
  }
}
