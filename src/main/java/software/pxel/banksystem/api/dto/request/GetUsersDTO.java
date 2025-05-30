package software.pxel.banksystem.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class GetUsersDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @Size(min = 13, max = 13, message = "Phone must be exactly 13 characters")
    @Pattern(
            regexp = "^\\+375(17|29|33|44)[0-9]{7}$",
            message = "Invalid phone format"
    )
    private String phone;

    @Size(max = 500, message = "Name must be at most 500 characters")
    private String name;

    @Size(max = 200, message = "Email must be at most 200 characters")
    @Email(message = "Invalid email format")
    private String email;

}
