import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/Rx';
import { UserPreview } from '../../models/user/user-preview';

@Injectable({
  providedIn: 'root'
})
export class ContactsService {
  private contacts = new BehaviorSubject<UserPreview[]>([]);

  constructor() { }

  getContact(id: number): UserPreview {
    return this.contacts.getValue().find(contact => contact.id === id);
  }

  setContacts(contacts: UserPreview[]) {
    this.contacts.next(contacts);
  }
}
