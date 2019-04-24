import { TestBed, async, inject, ComponentFixture } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { RxStompService } from '@stomp/ng2-stompjs';
import { ToastrModule } from 'ngx-toastr';
import { Observable } from 'rxjs/Rx';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { InputWithValidationDirective } from './directives/input-with-validation.directive';
import { ContactsSidebarComponent } from './components/contacts-sidebar/contacts-sidebar.component';
import { ContactSidebarComponent } from './components/contacts-sidebar/contact-sidebar/contact-sidebar.component';
import { ChatsListComponent } from './components/chats-list/chats-list.component';
import { ChatComponent } from './components/chats-list/chat/chat.component';
import { ChatSelectorComponent } from './components/chats-list/chat-selector/chat-selector.component';
import { UserService } from './services/user/user.service';
import { AuthorizationService } from './services/authorization/authorization.service';

describe('AppComponent', () => {
  let userService: UserService;
  let fixture: ComponentFixture<AppComponent>;
  let app;

  const contacts = [{
    id: 0,
    login: 'login0',
    name: 'name0',
    surname: 'surname0'
  }, {
    id: 1,
    login: 'login1',
    name: 'name1',
    surname: 'surname1'
  }];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        ContactsSidebarComponent,
        ContactSidebarComponent,
        InputWithValidationDirective,
        ChatsListComponent,
        ChatComponent,
        ChatSelectorComponent
      ],
      imports: [
        FormsModule,
        NgbTooltipModule,
        OwlDateTimeModule,
        HttpClientTestingModule,
        OwlNativeDateTimeModule,
        RouterTestingModule,
        ToastrModule.forRoot()
      ],
      providers: [ RxStompService ]
    }).compileComponents();
  }));

  beforeEach(async(inject([ UserService, AuthorizationService ], (service, authorizationService) => {
    userService = service;
    spyOn(userService, 'loadAllChatContacts').and.returnValue(Observable.of(contacts));
    authorizationService.saveAuthenticationDetails('token', 'login');
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.debugElement.componentInstance;
    fixture.detectChanges();
    fixture.autoDetectChanges(true);
  })));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('should add chat after contact selection from sidebar', () => {
    expect(app.selectedContacts.length).toBe(0);
    fixture.debugElement.nativeElement.querySelector('app-contact-sidebar div').dispatchEvent(new Event('click'));
    expect(app.selectedContacts.length).toBe(1);
    expect(app.selectedContacts[0].id).toBe(contacts[0].id);
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-chat').length).toBe(1);
  });

  it('should not remove contact from sidebar after close chat', () => {
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-contact-sidebar').length).toBe(contacts.length);
    fixture.debugElement.nativeElement.querySelector('app-contact-sidebar div').dispatchEvent(new Event('click'));
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-chat').length).toBe(1);
    expect(app.selectedContacts.length).toBe(1);
    fixture.debugElement.nativeElement.querySelector('app-chat .close-button').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-chat').length).toBe(0);
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-contact-sidebar').length).toBe(contacts.length);
  });
});
