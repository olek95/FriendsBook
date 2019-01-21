import { Component, OnInit } from '@angular/core';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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
        this.authorizationService.logIn(this.name, this.password).subscribe(response => {
            localStorage.setItem('token', response.headers.get('Authorization'));
            this.router.navigate(['/home']);
        }, err => {
            this.toastrService.error("Not correct credentials passed", 'Authorization Error');
        })
    }
}
