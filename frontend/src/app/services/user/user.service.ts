import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { UserPreview } from '../../models/user/user-preview';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private message;
  private wsstate;

  constructor(private http: HttpClient) { }

  loadAllChatContacts(): Observable<UserPreview[]>{
    return this.http.get<UserPreview[]>('/chat-contacts');
  }
}
