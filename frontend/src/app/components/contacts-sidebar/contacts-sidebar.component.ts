import {Component, EventEmitter, OnInit, Output} from '@angular/core';

import { UserService } from '../../services/user/user.service';
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

  constructor(private userService: UserService) {

    this.userService.loadAllChatContacts().subscribe(users => {
        this.users = users;
    });
  }

  ngOnInit() {
  }

  selectContact(selectedContact: UserPreview) {
    this.onContactSelection.emit(selectedContact);
  }

}
