import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { OwlDateTimeModule, OwlNativeDateTimeModule, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';
import { NgbTooltipModule, NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';

import { InputWithValidationDirective } from './directives/input-with-validation.directive';

import { InterceptorService } from './services/interceptor/interceptor.service';
import { RequiredWithTrimDirective } from './directives/required-with-trim.directive';
import { HomeComponent } from './components/home/home.component';
import { ContactsSidebarComponent } from './components/contacts-sidebar/contacts-sidebar.component';
import { ContactSidebarComponent } from './components/contacts-sidebar/contact-sidebar/contact-sidebar.component';
import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { stompConfig } from './configuration/stop-config';
import { AuthorizedAccessGuard } from './services/guards/authorized-access-guard.service';
import { NotAuthorizedAccessGuard } from './services/guards/not-authorized-access-guard.service';

const routes: Routes = [
  { path: '', component: RegistrationComponent, canActivate: [ NotAuthorizedAccessGuard ] },
  { path: 'home', component: HomeComponent, canActivate: [ AuthorizedAccessGuard ] }
];

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        InputWithValidationDirective,
        RequiredWithTrimDirective,
        HomeComponent,
        ContactsSidebarComponent,
        ContactSidebarComponent
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
        StompService,
        AuthorizedAccessGuard,
        NotAuthorizedAccessGuard,
        {
          provide: OWL_DATE_TIME_LOCALE,
          useValue: 'pl'
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: InterceptorService,
            multi: true
        },
        {
          provide: StompConfig,
          useValue: stompConfig
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
