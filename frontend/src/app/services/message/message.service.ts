import { Injectable } from '@angular/core';

import { RxStompService } from '@stomp/ng2-stompjs';
import { Message } from '../../models/message/message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private stompService: RxStompService) { }

  sendMessage(message: Message) {
    this.stompService.publish({
      destination: '/app/message',
      body: JSON.stringify(message)
    });
  }

  getMessage(onMessageReceiving: (message: string) => void) {
    return this.stompService.stompClient.subscribe('/topic/message', message => { onMessageReceiving(message.body)});
  }
}
