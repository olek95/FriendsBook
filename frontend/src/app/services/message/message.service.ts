import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Rx';
import { RxStompService } from '@stomp/ng2-stompjs';
import { Message } from '../../models/message/message';
import { AuthorizationService } from '../authorization/authorization.service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private stompService: RxStompService, private authorizationService: AuthorizationService, private http: HttpClient) { }

  sendMessage(message: Message, recipientName: string) {
    this.stompService.publish({
      destination: `/app/message/${recipientName}`,
      body: JSON.stringify(message)
    });
  }

  startListeningForMessage() {
    return this.stompService.watch(`/user/${this.authorizationService.getAuthenticationDetails().login}/queue/message`);
  }

  getConversationWithUser(correspondentId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`/message/${correspondentId}`);
  }
}
