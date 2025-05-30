package software.pxel.banksystem.service;

import software.pxel.banksystem.api.dto.request.CreateEmailDataDTO;
import software.pxel.banksystem.api.dto.request.UpdateEmailDataDTO;
import software.pxel.banksystem.api.dto.response.EmailDataDTO;

public interface EmailDataService {

    EmailDataDTO create(Long userId, CreateEmailDataDTO request);

    EmailDataDTO update(Long emailId, UpdateEmailDataDTO request);

    EmailDataDTO delete(Long emailId);
}
