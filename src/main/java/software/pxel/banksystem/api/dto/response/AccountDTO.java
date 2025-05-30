package software.pxel.banksystem.api.dto.response;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        BigDecimal balance,
        Long userId
) {}
