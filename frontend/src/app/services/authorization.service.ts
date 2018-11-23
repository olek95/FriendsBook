import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; 
import { User } from '../models/user';

@Injectable({
    providedIn: 'root'
})
export class AuthorizationService {

    constructor(private http: HttpClient) { }
    
    public logIn(user: User) {
        const headers = {headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa(user.login + ':' + user.password) 
            })
        }
        return this.http.get("http://localhost:8080/FriendsBook/account/login", headers);
    }
}
