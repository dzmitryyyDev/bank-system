package software.pxel.banksystem.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePhoneDataDTO(
        @NotBlank(message = "Phone must not be empty")
        @Size(min = 13, max = 13, message = "Invalid phone size, must be 13")
        @Pattern(
                regexp = "^\\+375(17|29|33|44)[0-9]{7}$",
                message = "Invalid phone format"
        )
        String phone
) {
}
