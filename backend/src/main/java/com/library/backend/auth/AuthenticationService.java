package com.library.backend.auth;

import com.library.backend.email.EmailService;
import com.library.backend.email.EmailTemplateName;
import com.library.backend.role.RoleRepository;
import com.library.backend.user.Token;
import com.library.backend.user.TokenRepository;
import com.library.backend.user.User;
import com.library.backend.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Value("${application.email.frontend.confirmation-url}")
    private String confirmationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        //extract the roles
        var tempRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Role not initialized"));
        //build the user
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(tempRole))
                .build();
        //save the user
        userRepository.save(user);
        //validate the account by email
        sendValidationEmail(user); //first step : generate and save the validation token then we send the email.
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(), //to
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                confirmationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedCode = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedCode;
    }

    private String generateActivationCode(int codeLength) {
        String codeCharacters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom(); //SecureRandom ensures that the randomly generated value is cryptographically secure
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = secureRandom.nextInt(codeCharacters.length()); // [0-9]
            codeBuilder.append(codeCharacters.charAt(randomIndex)); // randomIndex is int we need characters
        }
        return codeBuilder.toString();
    }
}
