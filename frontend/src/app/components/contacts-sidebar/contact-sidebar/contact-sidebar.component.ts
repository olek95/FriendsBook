import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { UserPreview } from '../../../models/user/user-preview';

@Component({
  selector: 'app-contact-sidebar',
  templateUrl: './contact-sidebar.component.html',
  styleUrls: ['./contact-sidebar.component.scss']
})
export class ContactSidebarComponent implements OnInit {
  @Input()
  user: UserPreview;
  @Output()
  onContactSelection = new EventEmitter();

  constructor() {
  }

  ngOnInit() {
  }

  selectContact() {
    this.onContactSelection.emit(this.user);
  }

}
