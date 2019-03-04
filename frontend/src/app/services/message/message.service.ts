import { Injectable } from '@angular/core';

import { RxStompService } from '@stomp/ng2-stompjs';
import { Message } from '../../models/message/message';
import { AuthorizationService } from '../authorization/authorization.service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private stompService: RxStompService, private authorizationService: AuthorizationService) { }

  sendMessage(message: Message, recipientName: string) {
    this.stompService.publish({
      destination: `/app/message/${recipientName}`,
      body: JSON.stringify(message)
    });
  }

  startListeningForMessage() {
    return this.stompService.watch(`/user/${this.authorizationService.getAuthenticationDetails().login}/queue/message`);
  }
}
