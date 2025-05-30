package software.pxel.banksystem.service;

import org.springframework.data.domain.Pageable;
import software.pxel.banksystem.api.dto.response.UserDTO;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<UserDTO> searchUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable);

}
