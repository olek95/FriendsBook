import { Component } from '@angular/core';
import { NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';
  
  constructor(private config: NgbTooltipConfig) {
      this.configTooltips();
  }
  
  configTooltips() {
      this.config.triggers = 'manual';
      this.config.autoClose = false;
  }
}
