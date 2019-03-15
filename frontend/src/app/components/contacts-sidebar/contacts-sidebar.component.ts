import {Component, EventEmitter, OnInit, Output} from '@angular/core';

import { UserService } from '../../services/user/user.service';
import { UserPreview } from '../../models/user/user-preview';
import { ContactsService } from '../../services/contacts/contacts.service';

@Component({
  selector: 'app-contacts-sidebar',
  templateUrl: './contacts-sidebar.component.html',
  styleUrls: ['./contacts-sidebar.component.scss']
})
export class ContactsSidebarComponent implements OnInit {
  @Output()
  onContactSelection = new EventEmitter();

  users: UserPreview[] = [];

  constructor(private userService: UserService, private contactsService: ContactsService) {

    this.userService.loadAllChatContacts().subscribe(users => {
        this.users = users;
        this.contactsService.setContacts(users);
    });
  }

  ngOnInit() {
  }

  selectContact(selectedContact: UserPreview) {
    this.onContactSelection.emit(selectedContact);
  }

}
