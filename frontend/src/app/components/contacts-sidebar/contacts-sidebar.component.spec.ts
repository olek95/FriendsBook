import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import  {AuthorizationService } from '../../services/authorization/authorization.service';
import { inject } from '@angular/core/testing';

import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { ContactsSidebarComponent } from './contacts-sidebar.component';
import { ContactSidebarComponent } from './contact-sidebar/contact-sidebar.component';
import { stompConfig } from '../../configuration/stomp-config';

describe('ContactsSidebarComponent', () => {
  let component: ContactsSidebarComponent;
  let fixture: ComponentFixture<ContactsSidebarComponent>;

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

  beforeEach(inject([AuthorizationService], (authorizationService) => {
    authorizationService.saveAuthenticationDetails('token', 0);
    fixture = TestBed.createComponent(ContactsSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
