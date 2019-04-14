import { async, ComponentFixture, TestBed, inject, tick, fakeAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { RxStompService } from '@stomp/ng2-stompjs';
import { Observable } from 'rxjs/Rx';
import { ChatsListComponent } from './chats-list.component';
import { ChatComponent } from './chat/chat.component';
import { ChatSelectorComponent } from './chat-selector/chat-selector.component';
import { AuthorizationService } from '../../services/authorization/authorization.service';
import { MessageService } from '../../services/message/message.service';
import { Message } from '../../models/message/message';
import { ContactsService } from '../../services/contacts/contacts.service';


class MockMessageService extends MessageService {
  static responseTimeout = 500;

  getConversationWithUser(correspondentId: number): Observable<Message[]> {
    return Observable.of([]).delay(MockMessageService.responseTimeout);
  }
}

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
      providers: [
        RxStompService,
        {
          provide: MessageService,
          useClass: MockMessageService
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(async(inject([ AuthorizationService ], (authorizationService) => {
    authorizationService.saveAuthenticationDetails('token', 'login4');
    fixture = TestBed.createComponent(ChatsListComponent);
    component = fixture.componentInstance;
    component.contacts = [];
    fixture.detectChanges();
    fixture.autoDetectChanges(true);
  })));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain two chats', () => {
    fillContactsArray(2);
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-chat').length).toBe(component.contacts.length);
  });

  it('should close proper chat after clicking on close button', () => {
    fillContactsArray(2);
    const expectedContactsNumber = component.contacts.length - 1;
    fixture.detectChanges();
    fixture.debugElement.nativeElement.querySelector('.close-button').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(component.contacts.length).toBe(expectedContactsNumber);
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-chat').length).toBe(expectedContactsNumber);
  });

  it('should activate opened chat', fakeAsync(() => {
    fillContactsArray(1);
    fixture.detectChanges();
    tick(MockMessageService.responseTimeout);
    expect(fixture.debugElement.query(By.directive(ChatComponent)).componentInstance.active).toBeTruthy();
  }));

  it('should deactivate chat after clicking on background', fakeAsync(() => {
    fillContactsArray(1);
    fixture.detectChanges();
    tick(MockMessageService.responseTimeout);
    document.dispatchEvent(new Event('click'));
    expect(fixture.debugElement.query(By.directive(ChatComponent)).componentInstance.active).toBeFalsy();
  }));

  it('should activate last opened chat', fakeAsync(() => {
    fillContactsArray(2);
    fixture.detectChanges();
    tick(MockMessageService.responseTimeout);
    const chats = fixture.debugElement.queryAll(By.directive(ChatComponent));
    expect(chats[0].componentInstance.active).toBeFalsy();
    expect(chats[1].componentInstance.active).toBeTruthy();
  }));

  it('should activate proper clicked chat', fakeAsync(() => {
    fillContactsArray(2);
    fixture.detectChanges();
    tick(MockMessageService.responseTimeout);
    fixture.debugElement.nativeElement.querySelector(`#conversation-${component.contacts[0].id}`).dispatchEvent(new Event('click'));
    const chats = fixture.debugElement.queryAll(By.directive(ChatComponent));
    expect(chats[0].componentInstance.active).toBeTruthy();
    expect(chats[1].componentInstance.active).toBeFalsy();
  }));

  it('should add new chat onMessageReceiving', inject([ ContactsService ], (contactsService) => {
    contactsService.setContacts([{
      id: 0,
      name: 'name1',
      surname: 'surname',
      login: 'login1'
    }]);
    const message = {
      content: 'someText',
      senderId: 0,
      recipientId: 1
    };
    component.onMessageReceiving(message);
    expect(component.conversations.get(0)[0]).toBe(message);
    expect(component.contacts.length).toBe(1);
  }));

  it('should not create chat selector when chats number is equal to allowed', () => {
    fillContactsArray(component.possibleVisibleChatsNumber);
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelector('app-chat-selector')).toBeNull();
  });

  it('should create chat selector when chats number is greater than allowed', () => {
    fillContactsArray(component.possibleVisibleChatsNumber + 1);
    fixture.detectChanges();
    const chatSelector = fixture.debugElement.query(By.directive(ChatSelectorComponent));
    expect(chatSelector).toBeDefined();
    expect(chatSelector.componentInstance.contacts[0]).toBe(component.contacts[0]);
  });

  it('should not create chat selector when chats number is less than allowed', () => {
    fillContactsArray(component.possibleVisibleChatsNumber - 1);
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelector('app-chat-selector')).toBeNull();
  });

  it('should replace proper chat after select contact from chat selector', () => {
    fillContactsArray(component.possibleVisibleChatsNumber + 1);
    const hiddenId = component.contacts[0].id;
    const visibleId = component.contacts[1].id;
    fixture.detectChanges();
    const chatSelector = fixture.debugElement.query(By.directive(ChatSelectorComponent));
    chatSelector.query(By.css('.selector')).nativeElement.dispatchEvent(new Event('click'));
    fixture.detectChanges();
    chatSelector.query(By.css('.list-option')).nativeElement.dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelector(`#conversation-${hiddenId}`)).toBeDefined();
    expect(chatSelector.componentInstance.contacts[0].id).toBe(visibleId);
  });

  it('should remove chat selector when all contacts from selector have been removed', () => {
    fillContactsArray(component.possibleVisibleChatsNumber + 1);
    fixture.detectChanges();
    fixture.debugElement.nativeElement.querySelector('.selector').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    fixture.debugElement.nativeElement.querySelector('.fa-times-circle').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelector('app-chat-selector')).toBeNull();
  });

  it('should remove chat selector when some chats have been removed and there is empty place', () => {
    fillContactsArray(component.possibleVisibleChatsNumber + 1);
    fixture.detectChanges();
    fixture.debugElement.nativeElement.querySelector('.close-button').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelector('app-chat-selector')).toBeNull();
  });

  const fillContactsArray = (chatsNumber: number) => {
    for (let i = 0; i < chatsNumber; i++) {
      component.contacts.push({
        id: i,
        name: `name${i}`,
        surname: `surname${i}`,
        login: `login${i}`
      });
    }
  };
});
