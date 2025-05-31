package software.pxel.banksystem.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "Email must not be empty")
        @Size(max = 200, message = "Email length must be at most 200 characters")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Password must not be empty")
        @Size(min = 8, max = 500, message = "Password length must be between 8 and 500 characters")
        String password
) {
}
