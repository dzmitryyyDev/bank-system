package software.pxel.banksystem.service.mapper;

import org.mapstruct.Mapper;
import software.pxel.banksystem.api.dto.response.AccountDTO;
import software.pxel.banksystem.dao.entity.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO toDTO(AccountEntity entity);

}
