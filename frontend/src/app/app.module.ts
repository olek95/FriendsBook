import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { NgbTooltipModule, NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegistrationComponent } from './components/registration/registration.component';

import { InputWithValidationDirective } from './directives/input-with-validation.directive';

import { InterceptorService } from './services/interceptor/interceptor.service';
import { RequiredWithTrimDirective } from './directives/required-with-trim.directive';

const routes: Routes = [{path: 'login', component: NavbarComponent}];

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        RegistrationComponent,
        InputWithValidationDirective,
        RequiredWithTrimDirective
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        NgbTooltipModule,
        RouterModule.forRoot(routes),
        ToastrModule.forRoot()
    ],
    providers: [
        NgbTooltipConfig,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: InterceptorService,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
