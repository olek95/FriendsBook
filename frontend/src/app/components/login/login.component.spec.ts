import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ToastrModule } from 'ngx-toastr';
import { LoginComponent } from './login.component';
import { RequiredWithTrimDirective } from '../../directives/required-with-trim.directive';
import { AuthorizationService } from '../../services/authorization/authorization.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  const LOGIN_INPUT_CSS = "input[name='login']";
  const PASSWORD_INPUT_CSS = "input[name='password']";

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        ToastrModule.forRoot()
      ],
      declarations: [
        LoginComponent,
        RequiredWithTrimDirective
      ],
      providers: [
        AuthorizationService
      ]
    }).compileComponents();
  }));

  beforeEach(async(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    fixture.autoDetectChanges(true);
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('login button should to be disabled on init', () => {
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
  });

  it('login button should to be enabled when fields are filled', () => {
    changeInputValue(LOGIN_INPUT_CSS, 'name');
    changeInputValue(PASSWORD_INPUT_CSS, 'password');
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeFalsy();
  });

  it('login button should to be enabled when fields are empty', () => {
    changeInputValue(LOGIN_INPUT_CSS, 'name');
    changeInputValue(PASSWORD_INPUT_CSS, 'password');
    changeInputValue(LOGIN_INPUT_CSS, '');
    changeInputValue(PASSWORD_INPUT_CSS, '');
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
  });

  it('login button should to be disabled when only login or email field is filled', () => {
    changeInputValue(LOGIN_INPUT_CSS, 'name');
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
  });

  it('login button should to be disabled when only password field is filled', () => {
    changeInputValue(PASSWORD_INPUT_CSS, 'password');
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
  });

  it('login button should to be disabled when login field is filled with using whitespaces', () => {
    changeInputValue(LOGIN_INPUT_CSS, '     ');
    changeInputValue(PASSWORD_INPUT_CSS, 'password');
    expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
  });

  const changeInputValue = function(css: string, value: string) {
    const input = fixture.debugElement.nativeElement.querySelector(css);
    input.value = value;
    input.dispatchEvent(new Event('input'));
  }
});
