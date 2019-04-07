import { Component, ElementRef, HostListener, Input, OnInit, QueryList, ViewChildren } from '@angular/core';
import { UserPreview } from '../../models/user/user-preview';
import { MessageService } from '../../services/message/message.service';
import { Message } from '../../models/message/message';
import { ContactsService } from '../../services/contacts/contacts.service';
import { ChatComponent } from './chat/chat.component';
import { ChatSelectorComponent } from './chat-selector/chat-selector.component';

@Component({
  selector: 'app-chats-list',
  templateUrl: './chats-list.component.html',
  styleUrls: ['./chats-list.component.scss']
})
export class ChatsListComponent implements OnInit {
  @Input()
  contacts: UserPreview[];

  @ViewChildren(ChatComponent, {read: ElementRef})
  chats: QueryList<ElementRef>;

  conversations = new Map<number, Message[]>();
  contactIdOfActiveChat: number;
  possibleVisibleChatsNumber: number;
  firstChatRightMargin = 229;
  notFirstChatRightMargin = 11;

  constructor(private messageService: MessageService, private contactsService: ContactsService, private element: ElementRef) {
  }

  ngOnInit() {
    this.messageService.startListeningForMessage().subscribe(message => {
      this.onMessageReceiving(JSON.parse(message.body));
    });
    this.calculatePossibleVisibleChatsNumber();
  }

  onMessageReceiving(message: Message) {
    if (!this.conversations.has(message.senderId)) {
      this.conversations.set(message.senderId, []);
    }
    if (!this.contacts.some(contact => contact.id === message.senderId)) {
      this.contacts.push(this.contactsService.getContact(message.senderId));
    }
    this.conversations.get(message.senderId).push(message);
  }

  addConversation(conversation: {recipientId: number, messages: Message[]}) {
    this.conversations.set(conversation.recipientId, conversation.messages);
    this.activateChat(conversation.recipientId);
  }

  removeConversation(correspondentId: number) {
    this.conversations.delete(correspondentId);
    this.contacts.splice(this.contacts.findIndex(contact => contact.id === correspondentId), 1);
  }

  activateChat(contactId: number) {
    this.contactIdOfActiveChat = contactId;
  }

  restoreChat(contact: UserPreview) {
    const restoredContactIndex = this.contacts.indexOf(contact);
    [this.contacts[this.contacts.length - this.possibleVisibleChatsNumber], this.contacts[restoredContactIndex]] =
      [this.contacts[restoredContactIndex], this.contacts[this.contacts.length - this.possibleVisibleChatsNumber]];
  }

  @HostListener('document:click', ['$event'])
  deactivateAllChatsOnOutsideClick($event) {
    if (!$event.path.some(element => element.id && element.id.includes(this.contactIdOfActiveChat))) {
      this.contactIdOfActiveChat = -1;
    }
  }

  @HostListener('window:resize')
  calculatePossibleVisibleChatsNumber() {
    let sumChatWidths = 0;
    this.possibleVisibleChatsNumber = 0;
    sumChatWidths += this.firstChatRightMargin + ChatComponent.WIDTH + this.notFirstChatRightMargin + ChatSelectorComponent.WIDTH;
    while(this.element.nativeElement.offsetWidth > sumChatWidths) {
      this.possibleVisibleChatsNumber++;
      sumChatWidths += this.notFirstChatRightMargin + ChatComponent.WIDTH;
    }
  }
}
