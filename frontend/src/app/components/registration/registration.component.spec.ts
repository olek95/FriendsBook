import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { RegistrationComponent } from './registration.component';
import { InputWithValidationDirective } from '../../directives/input-with-validation.directive';
import { RequiredWithTrimDirective } from '../../directives/required-with-trim.directive';

describe('RegistrationComponent', () => {
    let component: RegistrationComponent;
    let fixture: ComponentFixture<RegistrationComponent>;
    const textFields = ['name', 'surname', 'login', 'password'];

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
              FormsModule,
              NgbTooltipModule,
              ToastrModule.forRoot(),
              OwlDateTimeModule,
              HttpClientTestingModule,
              OwlNativeDateTimeModule,
              BrowserAnimationsModule
            ],
            declarations: [
              RegistrationComponent,
              InputWithValidationDirective,
              RequiredWithTrimDirective
            ]
        }).compileComponents();
    }));

    beforeEach(async(() => {
        fixture = TestBed.createComponent(RegistrationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
        fixture.autoDetectChanges(true);
    }));

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('registration button should to be disabled on init', () => {
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('registration button should to be enabled when all fields are filled', () => {
      fillAllFields();
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeFalsy();
    });

    it('registration button should to be disabled when at least one field is empty', () => {
      fillAllFields();
      const allFields = [...textFields];
      allFields.push('mail', 'birth-date');
      allFields.forEach(name => {
        const oldValue = fixture.debugElement.nativeElement.querySelector("input[name='" + name + "']").value;
        changeInputValue(name, '');
        expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
        changeInputValue(name, oldValue);
      });
    });

    it('registration button should to be disabled when login has "@" character', () => {
      fillAllFields();
      changeInputValue('login', 'mail@mail.mail');
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('registration button should to be disabled when mail has only user id', () => {
      fillAllFields();
      changeInputValue('mail', 'mail');
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('registration button should to be disabled when mail has trailing "@" character', () => {
      fillAllFields();
      changeInputValue('mail', 'mail@');
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('registration button should to be disabled when mail has trailing dot character', () => {
      fillAllFields();
      changeInputValue('mail', 'mail@mail.mail.');
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('registration button should to be disabled when date has not correct format', () => {
      fillAllFields();
      changeInputValue('birth-date', 'date');
      expect(fixture.debugElement.nativeElement.querySelector('button').disabled).toBeTruthy();
    });

    it('calendar popup should to be open after clicking on calendar button', () => {
      fixture.debugElement.nativeElement.querySelector('.fa-calendar').click();
      expect(document.getElementsByClassName('owl-dt-popup-container').length).toBeGreaterThan(0);
    });

    const changeInputValue = function(name: string, value: string) {
      const input = fixture.debugElement.nativeElement.querySelector("input[name='" + name + "']");
      input.value = value;
      input.dispatchEvent(new Event('input'));
    };

    const fillAllFields = function() {
      textFields.forEach(name => {
        changeInputValue(name, 'value');
      });
      changeInputValue('mail', 'mail@mail.mail');
      changeInputValue('birth-date', '12.10.2018');
    };
});
