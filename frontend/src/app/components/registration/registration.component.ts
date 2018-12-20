import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user/user';
import { Gender } from '../../models/user/gender';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    user = new User();
    gender = Gender;

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService) {
        this.user.gender = Gender.FEMALE;
    }

    ngOnInit() {
    }

    register() {
        this.authorizationService.register(this.user).subscribe(response => {
        }, err => {
            this.toastrService.error(err.status === 409 ? "User already exists" : "Could not create user",
                "Registration failed");
        })
    }

    isLoginCorrect() {
        return !this.user.login || !this.user.login.includes('@');
    }
}
