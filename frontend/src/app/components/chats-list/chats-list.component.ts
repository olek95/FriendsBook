import { Component, HostListener, Input, OnInit } from '@angular/core';
import { UserPreview } from '../../models/user/user-preview';
import { MessageService } from '../../services/message/message.service';
import { Message } from '../../models/message/message';
import { ContactsService } from '../../services/contacts/contacts.service';

@Component({
  selector: 'app-chats-list',
  templateUrl: './chats-list.component.html',
  styleUrls: ['./chats-list.component.scss']
})
export class ChatsListComponent implements OnInit {
  @Input()
  contacts: UserPreview[];

  conversations = new Map<number, Message[]>();
  contactIdOfActiveChat: number;

  constructor(private messageService: MessageService, private contactsService: ContactsService) {}

  ngOnInit() {
    this.messageService.startListeningForMessage().subscribe(message => {
      this.onMessageReceiving(JSON.parse(message.body));
    });
  }

  onMessageReceiving(message: Message) {
    if (!this.conversations.has(message.senderId)) {
      this.conversations.set(message.senderId, []);
    }
    if (!this.contacts.some(contact => contact.id === message.recipientId)) {
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

  @HostListener('document:click', ['$event'])
  deactivateAllChatsOnOutsideClick($event) {
    if (!$event.path.some(element => element.id && element.id.includes(this.contactIdOfActiveChat))) {
      this.contactIdOfActiveChat = -1;
    }
  }
}
