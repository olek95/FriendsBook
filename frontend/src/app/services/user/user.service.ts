import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import {StompService, StompState} from "@stomp/ng2-stompjs";
import {map} from "rxjs/internal/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private message;
  private wsstate;

  constructor(private stompService: StompService) { }

  // loadAllChatContacts(userId: number): Observable<UserPreview[]>{
  //   return this.http.get<UserPreview[]>('/chat-contacts', {params: {'userId': userId + ''}});
  // }

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
