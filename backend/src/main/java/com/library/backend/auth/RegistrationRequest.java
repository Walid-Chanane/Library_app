package com.library.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "Firstname is required!")
    @NotBlank(message = "Firstname is required!")
    private String firstName;

    @NotEmpty(message = "Firstname is required!")
    @NotBlank(message = "Firstname is required!")
    private String lastName;

    @NotEmpty(message = "Firstname is required!")
    @NotBlank(message = "Firstname is required!")
    @Email(message = "Incorrect email format provided")
    private String email;

    @NotEmpty(message = "Firstname is required!")
    @NotBlank(message = "Firstname is required!")
    @Size(min = 8, message = "Password should contain at least 8 characters")
    private String password;
}
