package com.library.backend.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED) // default http response ?
    // will automatically require to add the "throws MessagingException" due to MimeMessage requiring it in the EmailService
    // (by following the method calls we will end up there)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/activate_account")
    public void activateAccount(@RequestParam String activationToken) throws MessagingException {
        authenticationService.activateAccount(activationToken);
    }
}
