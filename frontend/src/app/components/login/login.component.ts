import { Component, OnInit } from '@angular/core';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { AuthenticationDetails } from '../../models/user/authentication-details';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    name: string;
    password: string;

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService, private router: Router) { }

    ngOnInit() {
    }

    logIn() {
        this.authorizationService.logIn(this.name, this.password).subscribe((response: any) => {
          this.authorizationService.saveAuthenticationDetails(response.headers.get('Authorization'), response.body.id);
          this.router.navigate(['/home']);
        }, err => {
            this.toastrService.error("Not correct credentials passed", 'Authorization Error');
        })
    }
}
