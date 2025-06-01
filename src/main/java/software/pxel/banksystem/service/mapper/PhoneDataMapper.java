package software.pxel.banksystem.service.mapper;

import org.mapstruct.Mapper;
import software.pxel.banksystem.api.dto.response.PhoneDataDTO;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;

@Mapper(componentModel = "spring")
public interface PhoneDataMapper {

    PhoneDataDTO toDTO(PhoneDataEntity entity);

}
