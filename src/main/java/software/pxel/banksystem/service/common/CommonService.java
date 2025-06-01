package software.pxel.banksystem.service.common;

import org.springframework.stereotype.Component;
import software.pxel.banksystem.api.exception.ErrorMessages;
import software.pxel.banksystem.api.exception.ForbiddenException;
import software.pxel.banksystem.dao.entity.UserEntity;

@Component
public class CommonService {

    public void validateOwnership(UserEntity user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(ErrorMessages.USER_CAN_ONLY_MODIFY_OWN_DATA);
        }
    }
}
