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
    component.contacts = [{
      id: 0,
      login: 'login0',
      name: 'name0',
      surname: 'surname0'
    }];
    fixture.detectChanges();
    fixture.autoDetectChanges(true);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show contacts list after clicking on selector', () => {
    fixture.debugElement.nativeElement.querySelector('.selector').dispatchEvent(new Event('click'));
    expect(fixture.debugElement.nativeElement.querySelector('.chat-select-list')).toBeDefined();
  });

  it('should hide contacts list when selector is not clicked', () => {
    expect(fixture.debugElement.nativeElement.querySelector('.chat-select-list')).toBeNull();
  });

  it('should hide contacts list after double clicking on selector', () => {
    fixture.debugElement.nativeElement.querySelector('.selector').dispatchEvent(new Event('click'));
    fixture.debugElement.nativeElement.querySelector('.selector').dispatchEvent(new Event('click'));
    expect(fixture.debugElement.nativeElement.querySelector('.chat-select-list')).toBeNull();
  });

  it('should close selector after contact selection', () => {
    fixture.debugElement.nativeElement.querySelector('.selector').dispatchEvent(new Event('click'));
    fixture.debugElement.nativeElement.querySelector('.list-option').dispatchEvent(new Event('click'));
    expect(fixture.debugElement.nativeElement.querySelector('.chat-select-list')).toBeNull();
  });
});
