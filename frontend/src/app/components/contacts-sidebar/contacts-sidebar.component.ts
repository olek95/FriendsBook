import {Component, EventEmitter, OnInit, Output} from '@angular/core';

import { UserService } from '../../services/user/user.service';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { UserPreview } from '../../models/user/user-preview';

@Component({
  selector: 'app-contacts-sidebar',
  templateUrl: './contacts-sidebar.component.html',
  styleUrls: ['./contacts-sidebar.component.scss']
})
export class ContactsSidebarComponent implements OnInit {
  @Output()
  onContactSelection = new EventEmitter();

  users: UserPreview[] = [];

  constructor(private userService: UserService, authService: AuthorizationService) {

    this.userService.loadAllChatContacts(authService.getAuthenticationDetails().id)
      .subscribe(users => {
        this.users = users;
    });
  }

  ngOnInit() {
  }

  selectContact(selectedContact: UserPreview) {
    console.log(selectedContact);
    this.onContactSelection.emit(selectedContact);
  }

}
