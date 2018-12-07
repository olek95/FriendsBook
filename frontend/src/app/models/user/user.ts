import { Gender } from './gender';

export class User {
    login: string;
    password: string;
    email: string;
    name: string;
    surname: string;
    birthDate: Date;
    gender: Gender;

    constructor() {}
}
