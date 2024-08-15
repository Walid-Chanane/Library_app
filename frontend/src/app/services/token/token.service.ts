import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  //setter method (save the returned token after authentication)
  set token(token: string){
    localStorage.setItem('token', token);
  }

  //getter method
  get token(){
    return localStorage.getItem('token') as string
  }
}
