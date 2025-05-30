package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;

    public TransferServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TransferDTO create(CreateTransferDTO createTransferDTO) {
        //TODO: added getting fromUserId from JWT claim "USER_ID"
        Long fromUserId = 1L;

        UserEntity fromUser = userRepository.findByIdWithAccountForUpdate(fromUserId)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        Long toUserId = createTransferDTO.toUserId();
        if (fromUser.getId().equals(toUserId)) {
            throw new BadRequestException("Cannot transfer to yourself");
        }

        UserEntity toUser = userRepository.findByIdWithAccountForUpdate(toUserId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Target user with id %s not found", toUserId)));

        if (fromUser.getAccount().getBalance().compareTo(createTransferDTO.amount()) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        fromUser.getAccount().setBalance(fromUser.getAccount().getBalance().subtract(createTransferDTO.amount()));
        toUser.getAccount().setBalance(toUser.getAccount().getBalance().add(createTransferDTO.amount()));

        return new TransferDTO(fromUser.getId(), toUser.getId(), createTransferDTO.amount());
    }

}
