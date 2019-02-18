import { Component, Input, OnInit } from '@angular/core';
import { UserPreview } from '../../models/user/user-preview';

@Component({
  selector: 'app-chats-list',
  templateUrl: './chats-list.component.html',
  styleUrls: ['./chats-list.component.scss']
})
export class ChatsListComponent implements OnInit {
  @Input()
  contacts: UserPreview[];

  constructor() { }

  ngOnInit() {
  }

}
