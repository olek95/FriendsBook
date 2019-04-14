import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbTooltipConfig, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';

import { InputWithValidationDirective } from './input-with-validation.directive';


@Component({
  template: `<input type="email" [(ngModel)]="mail" email #inputModel="ngModel" ngbTooltip="Some error" #tooltip="ngbTooltip"
                    [errors]="inputModel.errors" [tooltip]="tooltip" appInputWithValidation>`
})
class TestMailInputComponent {
  mail: string;
}

@Component({
  template: `<input [(ngModel)]="text" #inputModel="ngModel" ngbTooltip="Some error" #tooltip="ngbTooltip"
                    [errors]="inputModel.errors" [tooltip]="tooltip" pattern="[^@]*" appInputWithValidation>`
})
class TestTextInputComponent {
  text: string;
}

@Component({
  template: `<div>
                <input [owlDateTime]="dt6" [(ngModel)]="date" #dateInput="ngModel" [errors]="dateInput.errors" [tooltip]="tooltip" 
                       ngbTooltip="Some error" #tooltip="ngbTooltip" appInputWithValidation>
                <owl-date-time [pickerType]="'calendar'" #dt6></owl-date-time>
            </div>`
})
class TestDateInputComponent {
  date: Date;
}

describe('InputWithValidationDirective', () => {
  let emailFixture: ComponentFixture<TestMailInputComponent>;
  let textFixture: ComponentFixture<TestTextInputComponent>;
  let dateFixture: ComponentFixture<TestDateInputComponent>;
  let component: TestMailInputComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NgbTooltipModule,
        FormsModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule
      ],
      declarations: [
        TestMailInputComponent,
        TestTextInputComponent,
        TestDateInputComponent,
        InputWithValidationDirective
      ],
      providers: [
        NgbTooltipConfig
      ]
    }).compileComponents();
  }));

  beforeEach(async(() => {
    emailFixture = TestBed.createComponent(TestMailInputComponent);
    textFixture = TestBed.createComponent(TestTextInputComponent);
    dateFixture = TestBed.createComponent(TestDateInputComponent);
    component = emailFixture.componentInstance;
    emailFixture.detectChanges();
    emailFixture.autoDetectChanges(true);
    textFixture.detectChanges();
    textFixture.autoDetectChanges(true);
    dateFixture.detectChanges();
    dateFixture.autoDetectChanges(true);
  }));

  it('should create an instance', () => {
    expect(component).toBeTruthy();
  });

  it('is-invalid class when email input is not valid', () => {
    changeInputValue(emailFixture, 'mail');
    expect(emailFixture.debugElement.nativeElement.querySelector('input')).toHaveClass('is-invalid');
    expect(emailFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeTruthy();
  });

  it('valid email input', () => {
    changeInputValue(emailFixture, 'mail@mail.mail');
    expect(emailFixture.debugElement.nativeElement.querySelector('input')).not.toHaveClass('is-invalid');
    expect(emailFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeFalsy();
  });

  it('error tooltip should to close itself after input focusout event', () => {
    changeInputValue(emailFixture, 'mail');
    let input = emailFixture.debugElement.nativeElement.querySelector('input');
    input.dispatchEvent(new Event('focusout'));
    expect(input).toHaveClass('is-invalid');
    expect(emailFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeFalsy();
  });

  it('error tooltip should to open again when input is focused', () => {
    changeInputValue(emailFixture, 'mail');
    let input = emailFixture.debugElement.nativeElement.querySelector('input');
    input.dispatchEvent(new Event('focusout'));
    input.dispatchEvent(new Event('focus'));
    expect(input).toHaveClass('is-invalid');
    expect(emailFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeTruthy();
  });

  it('in-valid class when text value does not match to pattern', () => {
    changeInputValue(textFixture, 'mail@mail.mail');
    expect(textFixture.debugElement.nativeElement.querySelector('input')).toHaveClass('is-invalid');
    expect(textFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeTruthy();
  });

  it('valid text input', () => {
    changeInputValue(textFixture, 'Text');
    expect(textFixture.debugElement.nativeElement.querySelector('input')).not.toHaveClass('is-invalid');
    expect(textFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeFalsy();
  });

  it('in-valid class when date has not correct format', () => {
    changeInputValue(dateFixture, 'abc');
    expect(dateFixture.debugElement.nativeElement.querySelector('input')).toHaveClass('is-invalid');
    expect(dateFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeTruthy();
  });

  it('valid date input', () => {
    changeInputValue(dateFixture, '12/1/2018');
    expect(dateFixture.debugElement.nativeElement.querySelector('input')).not.toHaveClass('is-invalid');
    expect(dateFixture.debugElement.nativeElement.querySelector('.tooltip')).toBeFalsy();
  });

  const changeInputValue = function(fixture: ComponentFixture<TestMailInputComponent> | ComponentFixture<TestTextInputComponent>
                                    | ComponentFixture<TestDateInputComponent>, value: string) {
    const input = fixture.debugElement.nativeElement.querySelector('input');
    input.value = value;
    input.dispatchEvent(new Event('input'));
    input.dispatchEvent(new Event('keyup'));
  };
});
