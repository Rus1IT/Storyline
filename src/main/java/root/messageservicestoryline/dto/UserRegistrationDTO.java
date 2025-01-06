package root.messageservicestoryline.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record UserRegistrationDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Age is required")
        @Min(value = 1,message = "Age must be grater than 1")
        @Max(value = 120,message = "Age must be less than 120")
        Integer age,

        @Nullable
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
        String phone_number,

        @Nullable
        @Email(message = "Invalid email address")
        String email,

        @Nullable
        String google_account,

        @Nullable
        String github_account) {
}
