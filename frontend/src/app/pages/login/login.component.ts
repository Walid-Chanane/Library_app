import { Component } from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errors: Array<string> = [];

  //injecting needed services (aka @RequiredArgsConstructor)
  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {}

  login() {
    this.errors = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (response) => {
        this.tokenService.token = response.token as string
        this.router.navigate(['books'])
      },
      error: (err) => {
        // we can only get validation errors, there is no wrong email or password for login for example
        this.errors = err.error.validationErrors 
      }
    });
  }

  register() {
    this.router.navigate(['register'])
  }
}
