package software.pxel.banksystem.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.pxel.banksystem.api.dto.request.CreatePhoneDataDTO;
import software.pxel.banksystem.api.dto.request.UpdatePhoneDataDTO;
import software.pxel.banksystem.api.dto.response.PhoneDataDTO;
import software.pxel.banksystem.service.PhoneDataService;

@RestController
@RequestMapping("/api/v1/phones")
@Validated
@Tag(name = "Phones", description = "Operations related to user's phones")
public class PhoneDataController {

    private final PhoneDataService phoneDataService;

    public PhoneDataController(PhoneDataService phoneDataService) {
        this.phoneDataService = phoneDataService;
    }

    @Operation(
            summary = "Create a new phone number",
            description = "Adds a new phone number to the current user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Phone created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PhoneDataDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PhoneDataDTO> create(@Valid @RequestBody CreatePhoneDataDTO createPhoneDataDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneDataService.create(createPhoneDataDTO));
    }

    @Operation(
            summary = "Update an existing phone number",
            description = "Updates phone number by its ID for the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Phone updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PhoneDataDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user doesn't own this phone",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Phone data not found",
                            content = @Content)
            }
    )
    @PatchMapping("/{phoneId}")
    public ResponseEntity<PhoneDataDTO> update(
            @PathVariable(name = "phoneId") @Positive(message = "Phone ID must be a positive number") Long phoneId,
            @Valid @RequestBody UpdatePhoneDataDTO updatePhoneDataDTO
    ) {
        return ResponseEntity.ok(phoneDataService.update(phoneId, updatePhoneDataDTO));
    }

    @Operation(
            summary = "Delete a phone number",
            description = "Deletes a phone number by ID for the current user (must have at least one phone remaining)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Phone deleted successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PhoneDataDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden, user doesn't own this phone",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Phone data not found",
                            content = @Content)
            }
    )
    @DeleteMapping("/{phoneId}")
    public ResponseEntity<PhoneDataDTO> delete(@PathVariable(name = "phoneId") @Positive(message = "Phone ID must be a positive number") Long phoneId) {
        return ResponseEntity.ok(phoneDataService.delete(phoneId));
    }
}
