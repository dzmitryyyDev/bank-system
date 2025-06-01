package software.pxel.banksystem.service;

import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;

public interface TransferService {

    TransferDTO create(CreateTransferDTO createTransferDTO);

}
