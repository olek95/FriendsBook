import { Directive } from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";

@Directive({
  selector: '[appRequiredWithTrim]',
  providers: [{
    provide: NG_VALIDATORS,
    useExisting: RequiredWithTrimDirective,
    multi: true
  }]
})
export class RequiredWithTrimDirective implements Validator {

  constructor() { }

  validate(control: AbstractControl): ValidationErrors | any {
    return control.value && control.value.trim() ? null : {required: 'required'};
  }
}
