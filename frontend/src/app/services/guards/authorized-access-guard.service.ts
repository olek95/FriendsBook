import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree} from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { AuthorizationService } from '../authorization/authorization.service';


@Injectable({
  providedIn: 'root'
})
export class AuthorizedAccessGuard implements CanActivate {

  constructor(private authorizationService: AuthorizationService, private router: Router) { }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const signed = this.authorizationService.isSigned();
    if (!signed) {
      this.router.navigate(['']);
    }
    return signed;
  }
}
