import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { UserService } from './user.service';
import { stompConfig } from '../../configuration/stomp-config';

describe('UserService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [ HttpClientTestingModule ],
    providers: [
      StompService,
      {
        provide: StompConfig,
        useValue: stompConfig
      }]
  }));

  it('should be created', () => {
    const service: UserService = TestBed.get(UserService);
    expect(service).toBeTruthy();
  });
});
