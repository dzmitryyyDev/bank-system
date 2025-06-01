package software.pxel.banksystem.api.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEmailDataDTO(
        @NotBlank(message = "Email must not be empty")
        @Size(max = 200, message = "Email length must be at most 200 characters")
        @Email(message = "Email format is invalid")
        String email
) {}
