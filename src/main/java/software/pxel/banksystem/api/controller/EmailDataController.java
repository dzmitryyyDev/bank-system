package software.pxel.banksystem.api.controller;

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
public class EmailDataController {

    private final EmailDataService emailDataService;

    public EmailDataController(EmailDataService emailDataService) {
        this.emailDataService = emailDataService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<EmailDataDTO> create(
            @PathVariable(name = "userId") @Positive(message = "User ID must be a positive number") Long userId,
            @Valid @RequestBody CreateEmailDataDTO createEmailDataDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailDataService.create(userId, createEmailDataDTO));
    }

    @PatchMapping("/{emailId}")
    public ResponseEntity<EmailDataDTO> update(
            @PathVariable(name = "emailId") @Positive(message = "Email ID must be a positive number") Long emailId,
            @Valid @RequestBody UpdateEmailDataDTO updateEmailDataDTO
    ) {
        return ResponseEntity.ok(emailDataService.update(emailId, updateEmailDataDTO));
    }

    @DeleteMapping("/{emailId}")
    public ResponseEntity<EmailDataDTO> delete(@PathVariable(name = "emailId") @Positive(message = "Email ID must be a positive number") Long emailId) {
        return ResponseEntity.ok(emailDataService.delete(emailId));
    }
}
