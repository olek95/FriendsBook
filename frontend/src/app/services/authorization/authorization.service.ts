import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../../models/user/user';
import { AuthenticationDetails } from '../../models/user/authentication-details';

@Injectable({
    providedIn: 'root'
})
export class AuthorizationService {

    constructor(private http: HttpClient) {}

    public logIn(name: string, password: string) {
        return this.http.get('/account/login',
            {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + btoa(name + ':' + password)
                }),
                observe: 'response'
            });
    }

    public register(user: User) {
        return this.http.post('/account/register', user);
    }

    public getAuthenticationDetails(): AuthenticationDetails {
      return <AuthenticationDetails>JSON.parse(localStorage.getItem('authenticationDetails'));
    }

    public saveAuthenticationDetails(token: string, login: string) {
      const authenticationDetails: AuthenticationDetails = {
        token: token,
        login: login
      };
      localStorage.setItem('authenticationDetails', JSON.stringify(authenticationDetails));
    }

    public isSigned(): boolean {
      return !!localStorage.getItem('authenticationDetails');
    }
}
