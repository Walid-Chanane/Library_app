import { Component } from '@angular/core';
import { RegistrationRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registrationRequest: RegistrationRequest = {email: '', firstName: '', lastName: '', password: ''};
  errors: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ){}

  register(){
    this.errors = []
    this.authService.register({
      body: this.registrationRequest
    }).subscribe()
  }

  login(){
    this.router.navigate(['login'])
  }
}
