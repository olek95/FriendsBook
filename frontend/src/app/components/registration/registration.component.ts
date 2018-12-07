import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user/user';
import { Gender } from '../../models/user/gender';
import { AuthorizationService } from '../../services/authorization.service';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    user = new User();
    gender = Gender;

    constructor(private authorizationService: AuthorizationService) {
        this.user.gender = Gender.FEMALE;
    }

    ngOnInit() {
    }

    register() {
        this.authorizationService.register(this.user).subscribe(response => {
            console.log("OK");
            console.log(response);
        }, err => {
            console.log(err);

        })
    }

}
