package software.pxel.banksystem.service;

import software.pxel.banksystem.api.dto.request.LoginRequestDTO;
import software.pxel.banksystem.api.dto.response.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

}
