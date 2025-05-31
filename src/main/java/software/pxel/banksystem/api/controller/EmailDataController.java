package software.pxel.banksystem.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.pxel.banksystem.api.dto.request.CreateEmailDataDTO;
import software.pxel.banksystem.api.dto.request.UpdateEmailDataDTO;
import software.pxel.banksystem.api.dto.response.EmailDataDTO;
import software.pxel.banksystem.service.EmailDataService;

@RestController
@RequestMapping("/api/v1/emails")
@Validated
@Tag(name = "Emails", description = "Operations related to user's emails")
public class EmailDataController {

    private final EmailDataService emailDataService;

    public EmailDataController(EmailDataService emailDataService) {
        this.emailDataService = emailDataService;
    }

    @Operation(summary = "Create a new email for the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email created successfully",
                    content = @Content(schema = @Schema(implementation = EmailDataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmailDataDTO> create(
            @Parameter(description = "Email data to create", required = true)
            @Valid @RequestBody CreateEmailDataDTO createEmailDataDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailDataService.create(createEmailDataDTO));
    }

    @Operation(summary = "Update an existing email by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully",
                    content = @Content(schema = @Schema(implementation = EmailDataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, user doesn't own this email", content = @Content),
            @ApiResponse(responseCode = "404", description = "Email not found", content = @Content)
    })
    @PatchMapping("/{emailId}")
    public ResponseEntity<EmailDataDTO> update(
            @Parameter(description = "ID of the email to update", required = true, example = "1")
            @PathVariable(name = "emailId") @Positive(message = "Email ID must be a positive number") Long emailId,
            @Parameter(description = "Email data update info", required = true)
            @Valid @RequestBody UpdateEmailDataDTO updateEmailDataDTO
    ) {
        return ResponseEntity.ok(emailDataService.update(emailId, updateEmailDataDTO));
    }

    @Operation(summary = "Delete an email by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email deleted successfully",
                    content = @Content(schema = @Schema(implementation = EmailDataDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, user doesn't own this email", content = @Content),
            @ApiResponse(responseCode = "404", description = "Email not found", content = @Content)
    })
    @DeleteMapping("/{emailId}")
    public ResponseEntity<EmailDataDTO> delete(
            @Parameter(description = "ID of the email to delete", required = true, example = "1")
            @PathVariable(name = "emailId") @Positive(message = "Email ID must be a positive number") Long emailId) {
        return ResponseEntity.ok(emailDataService.delete(emailId));
    }
}
