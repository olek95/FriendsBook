import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user';
import { AuthorizationService } from '../../services/authorization.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    user = {login: '', password: '', email: ''};	

    constructor(private authorizationService: AuthorizationService) { }

    ngOnInit() {
    }
    
    logIn() {
        this.authorizationService.logIn(this.user).subscribe(response => {
            console.log(response);
        }, err => {
            console.log(err);
        })
    }
}
