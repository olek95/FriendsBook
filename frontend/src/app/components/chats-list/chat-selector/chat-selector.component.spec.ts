import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RxStompService } from '@stomp/ng2-stompjs';
import { ChatSelectorComponent } from './chat-selector.component';

describe('ChatSelectorComponent', () => {
  let component: ChatSelectorComponent;
  let fixture: ComponentFixture<ChatSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatSelectorComponent ],
      providers: [ RxStompService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
