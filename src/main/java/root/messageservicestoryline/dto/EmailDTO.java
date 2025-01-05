package root.messageservicestoryline.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(
        @NotBlank(message = "Recipient email is required")
        @Email(message = "Invalid email address")
        String to,

        @NotBlank(message = "Subject is required")
        String subject,

        @NotBlank(message = "Message body is required")
        String body
) {}
