package software.pxel.banksystem.api.controller;

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
public class PhoneDataController {

    private final PhoneDataService phoneDataService;

    public PhoneDataController(PhoneDataService phoneDataService) {
        this.phoneDataService = phoneDataService;
    }

    @PostMapping
    public ResponseEntity<PhoneDataDTO> create(@Valid @RequestBody CreatePhoneDataDTO createPhoneDataDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneDataService.create(createPhoneDataDTO));
    }

    @PatchMapping("/{phoneId}")
    public ResponseEntity<PhoneDataDTO> update(
            @PathVariable(name = "phoneId") @Positive(message = "Phone ID must be a positive number") Long phoneId,
            @Valid @RequestBody UpdatePhoneDataDTO updatePhoneDataDTO
    ) {
        return ResponseEntity.ok(phoneDataService.update(phoneId, updatePhoneDataDTO));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<PhoneDataDTO> delete(@PathVariable(name = "phoneId") @Positive(message = "Phone ID must be a positive number") Long phoneId) {
        return ResponseEntity.ok(phoneDataService.delete(phoneId));
    }
}
