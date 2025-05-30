package software.pxel.banksystem.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransferDTO(
        @NotNull
        Long toUserId,
        @NotNull
        @DecimalMin(value = "0.20", message = "Amount must be at least 0.20 BYN")
        BigDecimal amount
) {
}
