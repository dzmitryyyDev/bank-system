package software.pxel.banksystem.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;
import software.pxel.banksystem.service.TransferService;

@RestController
@RequestMapping("/api/v1/transfers")
@Validated
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferDTO> create(@Valid @RequestBody CreateTransferDTO createTransferDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.create(createTransferDTO));
    }
}
