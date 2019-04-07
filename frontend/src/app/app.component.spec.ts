import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { RxStompService } from '@stomp/ng2-stompjs';
import { ToastrModule } from 'ngx-toastr';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { InputWithValidationDirective } from './directives/input-with-validation.directive';
import { ContactsSidebarComponent } from './components/contacts-sidebar/contacts-sidebar.component';
import { ContactSidebarComponent } from './components/contacts-sidebar/contact-sidebar/contact-sidebar.component';
import { ChatsListComponent } from './components/chats-list/chats-list.component';
import { ChatComponent } from './components/chats-list/chat/chat.component';
import { ChatSelectorComponent } from './components/chats-list/chat-selector/chat-selector.component';

describe('AppComponent', () => {
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

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
