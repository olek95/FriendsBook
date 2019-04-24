import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { Observable } from 'rxjs/Rx';
import { ContactsSidebarComponent } from './contacts-sidebar.component';
import { ContactSidebarComponent } from './contact-sidebar/contact-sidebar.component';
import { stompConfig } from '../../configuration/stomp-config';
import { UserService } from '../../services/user/user.service';
import { ContactsService } from '../../services/contacts/contacts.service';

describe('ContactsSidebarComponent', () => {
  let component: ContactsSidebarComponent;
  let userService: UserService;
  let fixture: ComponentFixture<ContactsSidebarComponent>;
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
      imports: [ HttpClientTestingModule ],
      declarations: [
        ContactsSidebarComponent,
        ContactSidebarComponent
      ],
      providers: [
        StompService,
        {
          provide: StompConfig,
          useValue: stompConfig
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    userService = TestBed.get(UserService);
    spyOn(userService, 'loadAllChatContacts').and.returnValue(Observable.of(contacts));
    fixture = TestBed.createComponent(ContactsSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add contacts to sidebar', () => {
    expect(fixture.debugElement.nativeElement.querySelectorAll('app-contact-sidebar').length).toBe(contacts.length);
  });

  it('should cache contacts after loading', inject([ ContactsService ], contactsService => {
    contacts.forEach(contact => {
      expect(contact).toBe(contactsService.getContact(contact.id));
    });
  }));
});
