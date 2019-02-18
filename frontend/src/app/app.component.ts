import { Component } from '@angular/core';

import { NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';
import { AuthorizationService } from './services/authorization/authorization.service';
import { UserPreview } from './models/user/user-preview';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    selectedContacts: UserPreview[] = [];

    constructor(private config: NgbTooltipConfig, private authorizationService: AuthorizationService) {
        this.configTooltips();
    }

    configTooltips() {
        this.config.triggers = 'manual';
        this.config.autoClose = false;
    }

    changeSelectedContact(selectedContact: UserPreview) {
      this.selectedContacts.push(selectedContact);
    }
}
