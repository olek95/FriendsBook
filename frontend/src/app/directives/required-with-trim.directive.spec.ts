import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

import { RequiredWithTrimDirective } from './required-with-trim.directive';

@Component({
  template: `<form #formExample="ngForm">
                <input [(ngModel)]="text" name="text" appRequiredWithTrim>
            </form>`
})
class TestInputComponent {
  @ViewChild('formExample')
  form: NgForm;

  text: string;
}

describe('RequiredWithTrimDirective', () => {
  let fixture: ComponentFixture<TestInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule
      ],
      declarations: [
        RequiredWithTrimDirective,
        TestInputComponent
      ]
    }).compileComponents();
  }));

  beforeEach(async(() => {
    fixture = TestBed.createComponent(TestInputComponent);
    fixture.detectChanges();
    fixture.autoDetectChanges();
  }));

  it('should create an instance', () => {
    const directive = new RequiredWithTrimDirective();
    expect(directive).toBeTruthy();
  });

  it('empty input is invalid', () => {
    expect(fixture.componentInstance.form.controls['text'].valid).toBeFalsy();
  });

  it('filled input with using only whitespaces is invalid', () => {
    changeInputValue("            ");
    expect(fixture.componentInstance.form.controls['text'].valid).toBeFalsy();
  });

  it('filled input is valid', () => {
    changeInputValue('Text');
    expect(fixture.componentInstance.form.controls['text'].valid).toBeTruthy();
  });

  const changeInputValue = function(value: string) {
    const input = fixture.debugElement.nativeElement.querySelector('input');
    input.value = value;
    input.dispatchEvent(new Event('input'));
  };
});
