package software.pxel.banksystem.api.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.pxel.banksystem.api.dto.request.GetUsersDTO;
import software.pxel.banksystem.api.dto.response.UserDTO;
import software.pxel.banksystem.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(@Valid @ModelAttribute GetUsersDTO getUsersDTO, Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(
                getUsersDTO.getDateOfBirth(),
                getUsersDTO.getPhone(),
                getUsersDTO.getName(),
                getUsersDTO.getEmail(),
                pageable
        ));
    }
}
