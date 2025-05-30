package software.pxel.banksystem.service;

import software.pxel.banksystem.api.dto.request.CreatePhoneDataDTO;
import software.pxel.banksystem.api.dto.request.UpdatePhoneDataDTO;
import software.pxel.banksystem.api.dto.response.PhoneDataDTO;

public interface PhoneDataService {

    PhoneDataDTO create(Long userId, CreatePhoneDataDTO request);

    PhoneDataDTO update(Long phoneDataId, UpdatePhoneDataDTO request);

    PhoneDataDTO delete(Long phoneDataId);
}
