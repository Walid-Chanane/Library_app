package com.library.backend.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    /** Since the sendEmail function might be time-consuming we can't block the user until the email is sent,
     * so we need to send it asynchronously */
    @Async // we have to add @EnableAsync in the main app class after adding this annotation
    public void sendEmail(String to, String username,
                          EmailTemplateName emailTemplate, String confirmationUrl,
                          String activationCode, String subject) throws MessagingException {
        String templateName;
        if(emailTemplate == null){ //if no name is provided we will name our template "confirm-email"
            templateName = "confirm-email";
        }else{
            templateName = emailTemplate.name(); // else keep the name provided
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>(); //to pass parameters to our email template / html template
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activationCode", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("walaid.chane@world.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        //process the (html) template (located in /resources/templates)
        String template = templateEngine.process(templateName, context); //context in case we have parameters (like in our case)

        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }
}
