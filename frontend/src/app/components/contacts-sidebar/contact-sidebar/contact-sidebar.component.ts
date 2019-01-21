import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-contact-sidebar',
  templateUrl: './contact-sidebar.component.html',
  styleUrls: ['./contact-sidebar.component.scss']
})
export class ContactSidebarComponent implements OnInit {
  @Input()
  user: UserPreview;

  constructor() {
  }

  ngOnInit() {
  }

}
