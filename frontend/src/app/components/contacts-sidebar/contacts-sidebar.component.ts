import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-contacts-sidebar',
  templateUrl: './contacts-sidebar.component.html',
  styleUrls: ['./contacts-sidebar.component.scss']
})
export class ContactsSidebarComponent implements OnInit {
  users: UserPreview[] = [];

  constructor(private userService: UserService) {
    this.users.push({
      name: 'NAME',
      surname: 'SURNAME'
    },
    {
      name: 'NAME',
      surname: 'SURNAME'
    });

    this.userService.sendMessage();
    this.userService.getSocketStateObservable().subscribe(state => {
      console.log(state);
    });
    this.userService.getSocketDataObservable().subscribe(message => {
      console.log(message);
    });
  }

  ngOnInit() {
  }

}
