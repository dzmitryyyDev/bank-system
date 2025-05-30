package software.pxel.banksystem.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.pxel.banksystem.api.dto.response.UserDTO;
import software.pxel.banksystem.dao.entity.UserEntity;

@Mapper(
        componentModel = "spring",
        uses = {AccountMapper.class, EmailDataMapper.class, PhoneDataMapper.class}
)
public interface UserMapper {

    @Mapping(target = "emailData", source = "emailData")
    @Mapping(target = "phoneData", source = "phoneData")
    UserDTO toDTO(UserEntity entity);

}
