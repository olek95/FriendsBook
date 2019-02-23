import { Component, Input, OnInit } from '@angular/core';

import { UserPreview } from '../../../models/user/user-preview';
import { Message } from '../../../models/message/message';
import { AuthorizationService } from '../../../services/authorization/authorization.service';
import { MessageService } from '../../../services/message/message.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  @Input()
  contact: UserPreview;

  messages: Message[] = [];

  constructor(private authorizationService: AuthorizationService, private messageService: MessageService) { }

  ngOnInit() {
  }

  sendMessage(content) {
    const message = {
      content: content,
      senderId: this.authorizationService.getAuthenticationDetails().id,
      recipientId: this.contact.id
    };
    this.messageService.sendMessage(message);
    this.messageService.getMessage(message => this.onMessageReceiving(message));
    //this.messages.push(message);
  }

  onMessageReceiving(content: string) {
    const message = {
      content: content,
      senderId: 0,
      recipientId: 0
    };
    this.messages.push(message);
  }

}
