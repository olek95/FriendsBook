import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ToastrService } from 'ngx-toastr';
import { RxStompService } from '@stomp/ng2-stompjs';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { stompConfig } from '../../configuration/stomp-config';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    name: string;
    password: string;

    constructor(private authorizationService: AuthorizationService, private toastrService: ToastrService, private router: Router,
                private stompService: RxStompService) { }

    ngOnInit() {
    }

    logIn() {
        this.authorizationService.logIn(this.name, this.password).subscribe((response: any) => {
          const token = response.headers.get('Authorization');
          this.authorizationService.saveAuthenticationDetails(token, response.body.id);
          this.connectWebsocket(token);
          this.router.navigate(['/home']);
        }, err => {
            this.toastrService.error("Not correct credentials passed", 'Authorization Error');
        })
    }

    connectWebsocket(token: string) {
      stompConfig.brokerURL += "?token=" + token;
      this.stompService.configure(stompConfig);
      this.stompService.activate();
    }
}
