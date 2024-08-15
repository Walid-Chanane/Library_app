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
      const authRequest = req.clone({
        headers : new HttpHeaders({
          Authorization: 'Bearer ' + token
        })
      })
      return next.handle(authRequest)
    }
    return next.handle(req)
  }
  

}
