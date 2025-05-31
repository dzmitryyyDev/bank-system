package software.pxel.banksystem.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations related to user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get filtered list of users",
            description = "Returns paginated and filtered list of users by optional query parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(
            @Parameter(description = "Filter parameters") @Valid @ModelAttribute GetUsersDTO getUsersDTO,
            @Parameter(hidden = true) Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsers(
                getUsersDTO.getDateOfBirth(),
                getUsersDTO.getPhone(),
                getUsersDTO.getName(),
                getUsersDTO.getEmail(),
                pageable
        ));
    }
}
