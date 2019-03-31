import { Component, EventEmitter, HostBinding, Input, OnInit, Output } from '@angular/core';
import { UserPreview } from '../../../models/user/user-preview';

@Component({
  selector: 'app-chat-selector',
  templateUrl: './chat-selector.component.html',
  styleUrls: ['./chat-selector.component.scss']
})
export class ChatSelectorComponent implements OnInit {

  @Input()
  contacts: UserPreview[];
  @Output()
  onContactSelection = new EventEmitter<UserPreview>();
  @Output()
  onRemovingContact = new EventEmitter<number>();

  @HostBinding('style.width.px')
  selectorWidth = ChatSelectorComponent.WIDTH;

  static readonly WIDTH = 55.8;
  contactsListVisible = false;

  constructor() { }

  ngOnInit() {
  }

  selectContact(contact: UserPreview) {
    this.onContactSelection.emit(contact);
    this.contactsListVisible = false;
  }

  removeContact(contactId: number) {
    this.onRemovingContact.emit(contactId);
  }

}
