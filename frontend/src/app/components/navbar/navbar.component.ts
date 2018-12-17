import { Component, OnInit } from '@angular/core';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    name: string;
    password: string;

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService) { }

    ngOnInit() {
    }

    logIn() {
        this.authorizationService.logIn(this.name, this.password).subscribe(response => {
            console.log(response);
        }, err => {
          this.toastrService.error("Not correct credentials passed", 'Authorization Error');
        })
    }
}
