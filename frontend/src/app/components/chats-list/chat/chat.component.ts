import { Component, Input, OnInit } from '@angular/core';
import { UserPreview } from '../../../models/user/user-preview';
import { Message } from '../../../models/message/message';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  @Input()
  contact: UserPreview;

  messages: Message[] = [];

  constructor() { }

  ngOnInit() {
  }

  sendMessage(message) {
    this.messages.push({
      content: message
    });
  }

}
