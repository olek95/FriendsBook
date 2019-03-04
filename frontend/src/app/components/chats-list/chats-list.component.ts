import { Component, Input, OnInit } from '@angular/core';
import { UserPreview } from '../../models/user/user-preview';
import { MessageService } from '../../services/message/message.service';
import { Message } from '../../models/message/message';

@Component({
  selector: 'app-chats-list',
  templateUrl: './chats-list.component.html',
  styleUrls: ['./chats-list.component.scss']
})
export class ChatsListComponent implements OnInit {
  @Input()
  contacts: UserPreview[];

  messages = new Map<number, Message[]>();

  constructor(private messageService: MessageService) {
  }

  ngOnInit() {
    this.messageService.startListeningForMessage().subscribe(message => {
      this.onMessageReceiving(JSON.parse(message.body));
    });
  }

  onMessageReceiving(message: Message) {
    if (!this.messages.has(message.senderId)) {
      this.messages.set(message.senderId, []);
    }
    this.messages.get(message.senderId).push(message);
  }
}
