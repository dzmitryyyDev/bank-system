package software.pxel.banksystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.pxel.banksystem.api.dto.response.UserDTO;

import java.time.LocalDate;

public interface UserService {

    Page<UserDTO> getUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable);

}
