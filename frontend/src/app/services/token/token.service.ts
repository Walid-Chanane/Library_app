import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

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

  isTokenNotValid(){
    return !this.isTokenValid()
  }

  private isTokenValid(){
    const token = this.token
    if(!token){
      return false
    }
    // decode the token
    const jwtHelper: JwtHelperService = new JwtHelperService()
    //check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token)
    if(isTokenExpired){
      localStorage.clear()
      return false
    }
    return true
  }
}
