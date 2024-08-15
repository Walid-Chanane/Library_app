import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { ActivateAccount$Params } from '../../services/fn/authentication/activate-account';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message:string = ''
  isOkay:boolean = true
  submitted:boolean = false

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ){}

  onCodeCompleted(token: string){ // in the backend, if the activation token is expired an new token will automatically be sent to your email
    this.confirmAccount(token)
  }

  confirmAccount(activationToken: string){
    this.authService.activateAccount({activationToken}).subscribe({
      next: () => {
        this.message = 'Your account has been successfuly activated.'
        this.submitted = true
        this.isOkay = true
      },
      error: () => {
        this.message = 'The provided token has been expired or is invalid'
        this.submitted = true
        this.isOkay = false
      }
    })
  }

  redirectToLogin(){
    this.router.navigate(['login'])
  }
}
