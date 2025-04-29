import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from '../token/token.service';

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService
  ){}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenService.token
    if(token){
      const authRequest = req.clone({ // clone because we can not change the original request
        headers : new HttpHeaders({
          Authorization: 'Bearer ' + token
        })
      })
      //if there is a token return the new request with the token
      return next.handle(authRequest)
    }// else return the original one, which means to pass the the next interceptor (the next filter)
    return next.handle(req)
  }
  

}
