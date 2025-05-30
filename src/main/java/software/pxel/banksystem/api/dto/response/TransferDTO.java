package software.pxel.banksystem.api.dto.response;

import java.math.BigDecimal;

public record TransferDTO(
        Long fromUserId,
        Long toUserId,
        BigDecimal amount
) {
}
