import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';

import { UserPreview } from '../../../models/user/user-preview';
import { Message } from '../../../models/message/message';
import { AuthorizationService } from '../../../services/authorization/authorization.service';
import { MessageService } from '../../../services/message/message.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnChanges {
  @Input()
  contact: UserPreview;
  @Input()
  messages: Message[];
  @Input()
  active: boolean;
  @Output()
  onConversationLoaded = new EventEmitter<{recipientId: number, messages: Message[]}>();
  @Output()
  onClose = new EventEmitter<number>();
  @Output()
  onSelection = new EventEmitter<number>();

  @ViewChild('messageInput')
  messageInput;

  minimized = false;
  messageContent: string;
  static readonly WIDTH = 284;

  constructor(private authorizationService: AuthorizationService, private messageService: MessageService) {
  }

  ngOnInit() {
    this.messageService.getConversationWithUser(this.contact.id).subscribe((conversation: Message[]) => {
      this.messages = conversation || [];
      this.onConversationLoaded.emit({
        recipientId: this.contact.id,
        messages: this.messages
      });
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.messageInput) {
      changes.active.currentValue ? this.messageInput.nativeElement.focus() : this.messageInput.nativeElement.blur();
    }
  }

  sendMessage() {
    if (this.messageContent && this.messageContent.trim()) {
      const message = {
        content: this.messageContent,
        senderId: -1,
        recipientId: this.contact.id
      };
      this.messageService.sendMessage(message, this.contact.login);
      this.messages.push(message);
      this.messageContent = '';
    }
  }

  isReceivedMessage(message: Message): boolean {
    return message.senderId === this.contact.id;
  }

  hasDisplayedProfileIcon(message: Message, index: number): boolean {
    const nextIndex = index + 1;
    return this.isReceivedMessage(message) && (nextIndex >= this.messages.length
      || nextIndex < this.messages.length && !this.isReceivedMessage(this.messages[nextIndex]));
  }

  close() {
    this.onClose.emit(this.contact.id);
  }

  changeMinimalizationState() {
    this.minimized = !this.minimized;
    if (!this.minimized) {
      setTimeout(() => {
        this.onSelection.emit(this.contact.id);
      });
    }
  }

  select() {
    this.onSelection.emit(this.contact.id);
  }

  getActualWidth(): number {
    return this.minimized ? 194 : ChatComponent.WIDTH;
  }
}
