import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';

import { ToastrService } from 'ngx-toastr';
import { AuthorizationService } from '../../services/authorization/authorization.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    @Output()
    onSuccessfulLogin = new EventEmitter();

    name: string;
    password: string;

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService,
                private router: Router) { }

    ngOnInit() {
    }

    logIn() {
        this.authorizationService.logIn(this.name, this.password).subscribe((response: any) => {
          const token = response.headers.get('Authorization');
          this.authorizationService.saveAuthenticationDetails(token, this.name);
          this.onSuccessfulLogin.emit();
          this.router.navigate(['/home']);
        }, err => {
            this.toastrService.error("Not correct credentials passed", 'Authorization Error');
        })
    }
}
