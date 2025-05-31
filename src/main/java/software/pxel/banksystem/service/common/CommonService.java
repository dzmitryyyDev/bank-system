package software.pxel.banksystem.service.common;

import org.springframework.stereotype.Component;
import software.pxel.banksystem.api.exception.ForbiddenException;
import software.pxel.banksystem.dao.entity.UserEntity;

@Component
public class CommonService {

    public void validateOwnership(UserEntity user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("You can only modify your own data");
        }
    }
}
