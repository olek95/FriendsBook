import { Component } from '@angular/core';

import { NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';
import { RxStompService } from '@stomp/ng2-stompjs';
import { AuthorizationService } from './services/authorization/authorization.service';
import { UserPreview } from './models/user/user-preview';
import { stompConfig } from './configuration/stomp-config';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    selectedContacts: UserPreview[] = [];

    constructor(private config: NgbTooltipConfig, private authorizationService: AuthorizationService,
                private stompService: RxStompService) {
        this.configTooltips();
        if (this.authorizationService.isSigned()) {
          this.connectWebsocket();
        }
    }

    configTooltips() {
        this.config.triggers = 'manual';
        this.config.autoClose = false;
    }

    changeSelectedContact(selectedContact: UserPreview) {
      if (!this.selectedContacts.includes(selectedContact)) {
        this.selectedContacts.push(selectedContact);
      }
    }

    connectWebsocket() {
      stompConfig.brokerURL += `?token=${this.authorizationService.getAuthenticationDetails().token}`;
      this.stompService.configure(stompConfig);
      this.stompService.activate();
    }
}
