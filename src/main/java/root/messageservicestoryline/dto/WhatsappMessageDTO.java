package root.messageservicestoryline.dto;

import jakarta.validation.constraints.NotBlank;

public record WhatsappMessageDTO(
        @NotBlank(message = "Recipient number is required")
        String number,

        @NotBlank(message = "Message is required")
        String message
) {
}
