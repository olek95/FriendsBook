import { Directive, HostListener, HostBinding, Input, OnInit } from '@angular/core';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';

@Directive({
    selector: '[appInputWithValidation]',
})
export class InputWithValidationDirective implements OnInit {
    @HostListener('focus')
    onFocus = this.validate;
    @HostListener('keyup')
    onKeyUp = this.validate;
    @HostListener('focusout')
    closeTooltip = () => {
        this.tooltip.close();
    };
    @HostBinding('class.is-invalid')
    invalid: boolean;

    @Input()
    errors;
    @Input()
    tooltip: NgbTooltip;

    constructor() {}


    ngOnInit() {
        this.tooltip.tooltipClass = 'error';
    }

    validate() {
        this.invalid = !this.isDataCorrect();
        return this.invalid ? this.tooltip.open() : this.tooltip.close();
    }

    isDataCorrect() {
        return !this.errors || (!this.errors.owlDateTimeParse && !this.errors.email && !this.errors.pattern);
    }
}
