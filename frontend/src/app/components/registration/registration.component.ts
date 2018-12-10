import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user/user';
import { Gender } from '../../models/user/gender';
import { AuthorizationService } from '../../services/authorization.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    user = new User();
    gender = Gender;
    emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService) {
        this.user.gender = Gender.FEMALE;
    }

    ngOnInit() {
    }

    register() {
        console.log(this.user);
        this.authorizationService.register(this.user).subscribe(response => {
            console.log("OK");
            console.log(response);
        }, err => {
            this.toastrService.error(err.status === 409 ? "User already exists" : "Could not create user",
                "Registration failed");
        })
    }

}
