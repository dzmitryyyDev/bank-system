package software.pxel.banksystem.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.pxel.banksystem.api.dto.response.AccountDTO;
import software.pxel.banksystem.dao.entity.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "userId", source = "user.id")
    AccountDTO toDTO(AccountEntity entity);

}
