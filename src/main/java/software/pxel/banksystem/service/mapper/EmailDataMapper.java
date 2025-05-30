package software.pxel.banksystem.service.mapper;

import org.mapstruct.Mapper;
import software.pxel.banksystem.api.dto.response.EmailDataDTO;
import software.pxel.banksystem.dao.entity.EmailDataEntity;

@Mapper(componentModel = "spring")
public interface EmailDataMapper {

    EmailDataDTO toDTO(EmailDataEntity entity);

}