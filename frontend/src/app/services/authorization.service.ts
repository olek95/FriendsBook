import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../models/user/user';

@Injectable({
    providedIn: 'root'
})
export class AuthorizationService {

    constructor(private http: HttpClient) {}

    public logIn(name: string, password: string) {
        return this.http.get('http://localhost:8080/FriendsBook/account/login',
            {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + btoa(name + ':' + password)
                })
            });
    }

    public register(user: User) {
        return this.http.post('http://localhost:8080/FriendsBook/account/register', user);
    }
}
