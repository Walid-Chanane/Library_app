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
    }).subscribe({
      next: (response) => {
        this.router.navigate(['activate_account'])
      },
      error: (err) => {
        console.log(this.errors);
        if(err.error.validationErrors){
          this.errors = err.error.validationErrors
        } else {
          this.errors.push(err.error.error)
        }
      }
    })
  }

  login(){
    this.router.navigate(['login'])
  }
}
