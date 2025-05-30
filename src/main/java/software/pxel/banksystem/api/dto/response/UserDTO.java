package software.pxel.banksystem.api.dto.response;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        AccountDTO account,
        List<EmailDataDTO> emailData,
        List<PhoneDataDTO> phoneData
) {}
