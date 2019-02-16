import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { map } from 'rxjs/internal/operators';
import { UserPreview } from '../../models/user/user-preview';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private message;
  private wsstate;

  constructor(private stompService: StompService, private http: HttpClient) { }

  loadAllChatContacts(userId: number): Observable<UserPreview[]>{
    return this.http.get<UserPreview[]>('/chat-contacts', {params: {'userId': userId + ''}});
  }

  sendMessage() {
    this.stompService.publish('/app/hello');
  }

  getSocketDataObservable() {
    return this.stompService.subscribe("/topic/greetings");
  }

  public getSocketStateObservable() {
    return this.stompService.state.pipe(map((state: number) => StompState[state]));
  }
}
