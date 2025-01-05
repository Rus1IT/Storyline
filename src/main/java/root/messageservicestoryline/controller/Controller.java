package root.messageservicestoryline.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import root.messageservicestoryline.dto.EmailDTO;
import root.messageservicestoryline.email.EmailService;


@RestController
@AllArgsConstructor
public class Controller {

    private EmailService emailService;

    @PostMapping("/api/email/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        try {
            emailService.sendEmail(emailDTO);
            return ResponseEntity.ok("Email sent successfully");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email:" + emailDTO + " " + e.getMessage());
        }
    }
}
