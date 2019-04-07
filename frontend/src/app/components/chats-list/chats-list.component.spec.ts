import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

import { RxStompService } from '@stomp/ng2-stompjs';
import { ChatsListComponent } from './chats-list.component';
import { ChatComponent } from './chat/chat.component';
import { ChatSelectorComponent } from './chat-selector/chat-selector.component';
import { AuthorizationService } from '../../services/authorization/authorization.service';

describe('ChatsListComponent', () => {
  let component: ChatsListComponent;
  let fixture: ComponentFixture<ChatsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChatsListComponent,
        ChatComponent,
        ChatSelectorComponent
      ],
      imports: [
        FormsModule,
        HttpClientTestingModule
      ],
      providers: [ RxStompService ]
    })
    .compileComponents();
  }));

  beforeEach(inject([ AuthorizationService ], (authorizationService) => {
    fixture = TestBed.createComponent(ChatsListComponent);
    component = fixture.componentInstance;
    authorizationService.saveAuthenticationDetails('token', 'login');
    component.contacts = [];
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
