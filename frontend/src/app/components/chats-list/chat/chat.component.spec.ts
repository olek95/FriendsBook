import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { RxStompService } from '@stomp/ng2-stompjs';
import { ChatComponent } from './chat.component';

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatComponent ],
      imports: [
        FormsModule,
        HttpClientTestingModule
      ],
      providers: [ RxStompService ]
    })
    .compileComponents();
  }));

  beforeEach(async(() => {
    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
    component.contact = {
      id: 0,
      name: 'name',
      surname: 'surname',
      login: 'login'
    };
    fixture.detectChanges();
    fixture.autoDetectChanges(true);
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('input should to have a content when message is not empty', () => {
    const input = changeInputValue('input', 'someText');
    expect(component.messageContent).toBe(input.value);
  });

  it('should remove message from input after Enter key pressing', fakeAsync(() => {
    component.messages = [];
    const input = sendMessage('someText');
    fixture.detectChanges();
    tick();
    expect(input.value).toBe('');
    expect(component.messageContent).toBe('');
  }));

  it('should add sent message to list of messages', () => {
    component.messages = [];
    const text = 'someText';
    sendMessage(text);
    fixture.detectChanges();
    expect(component.messages.filter(message => message.content === text).length).toBe(1);
    expect(fixture.debugElement.nativeElement.querySelectorAll('.sent-message').length).toBe(1);
  });

  it('should not add sent message to list of messages when message has only empty chars', () => {
    component.messages = [];
    const text = '    ';
    sendMessage(text);
    expect(component.messages.filter(message => message.content === text).length).toBe(0);
    expect(fixture.debugElement.nativeElement.querySelectorAll('.sent-message').length).toBe(0);
  });

  it('should not add sent message to list of messages when nothing has been typed', () => {
    component.messages = [];
    sendMessage();
    expect(component.messages.length).toBe(0);
    expect(fixture.debugElement.nativeElement.querySelectorAll('.sent-message').length).toBe(0);
  });

  it('should display messages of first user on left side and for second user on right side', () => {
    component.messages = [{
      content: 'm1',
      recipientId: 1,
      senderId: 0
    }, {
      content: 'm2',
      recipientId: 0,
      senderId: 1
    }];
    fixture.detectChanges();
    const sentMessage = fixture.debugElement.nativeElement.querySelector('.sent-message').querySelector('.content');
    const receivedMessage = fixture.debugElement.nativeElement.querySelector('.received-message').querySelector('.content');
    expect(receivedMessage.textContent).toBe(component.messages[0].content);
    expect(sentMessage.textContent).toBe(component.messages[1].content);
    expect(sentMessage.offsetLeft).toBeGreaterThan(receivedMessage.offsetLeft);
  });

  it('should display profile icon next to last received message for each part', () => {
    component.messages = [{
      content: 'm1',
      recipientId: 1,
      senderId: 0
    }, {
      content: 'm2',
      recipientId: 1,
      senderId: 0
    }, {
      content: 'm3',
      recipientId: 0,
      senderId: 1
    }, {
      content: 'm4',
      recipientId: 1,
      senderId: 0
    }];
    fixture.detectChanges();
    const conversation = fixture.debugElement.nativeElement.querySelectorAll('.message');
    expect(conversation.length).toBe(component.messages.length);
    for (let i = 0; i < conversation.length; i++) {
      if (conversation[i].classList.contains('received-message')
        && ((i !== conversation.length - 1 && conversation[i + 1].classList.contains('sent-message')) || i === conversation.length - 1)) {
        expect(conversation[i].querySelector('.profile-picture')).toBeDefined();
      } else {
        expect(conversation[i].querySelector('.profile-picture')).toBeNull();
      }
    }
  });

  it('should to minimize after clicking on header', () => {
    const chat = fixture.debugElement.nativeElement.querySelector('.chat');
    const maximizeHeight = chat.offsetHeight;
    chat.querySelector('.header').dispatchEvent(new Event('click'));
    expect(chat.offsetHeight).toBeLessThan(maximizeHeight);
  });

  it('should maximize after clicking on minilized chat header', () => {
    component.minimized = true;
    fixture.detectChanges();
    const chat = fixture.debugElement.nativeElement.querySelector('.chat');
    const minimizeHeight = chat.offsetHeight;
    chat.querySelector('.header').dispatchEvent(new Event('click'));
    fixture.detectChanges();
    expect(chat.offsetHeight).toBeGreaterThan(minimizeHeight);
  });

  const changeInputValue = function(css: string, value: string) {
    const input = fixture.debugElement.nativeElement.querySelector(css);
    input.value = value;
    input.dispatchEvent(new Event('input'));
    return input;
  };

  const sendMessage = function(value?: string) {
    let input;
    if (value) {
      input = changeInputValue('input', value);
    }
    fixture.debugElement.query(By.css('form')).triggerEventHandler('ngSubmit', null);
    return input;
  };
});
