package software.pxel.banksystem.api.dto.response;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        Long userId,
        BigDecimal balance,
        BigDecimal initialBalance
) {
}
