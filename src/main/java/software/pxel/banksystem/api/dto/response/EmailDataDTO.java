package software.pxel.banksystem.api.dto.response;

public record EmailDataDTO(
        Long id,
        String email,
        Long userId
) {}
