package software.pxel.banksystem.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;
import software.pxel.banksystem.service.TransferService;

@RestController
@RequestMapping("/api/v1/transfers")
@Validated
@Tag(name = "Transfers", description = "Operations related to money transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(
            summary = "Create a new money transfer",
            description = "Creates a new transfer from the current user to another user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transfer created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransferDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<TransferDTO> create(@Valid @RequestBody CreateTransferDTO createTransferDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.create(createTransferDTO));
    }
}
