package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    public TransferServiceImpl(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public TransferDTO create(CreateTransferDTO createTransferDTO) {
        // get fromUserId from jwt token
        Long fromUserId = jwtUtils.getUserIdFromSecurityContext();

        // try to find user by fromUserId with PESSIMISTIC_WRITE lock
        UserEntity fromUser = userRepository.findByIdWithAccountForUpdate(fromUserId)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        // check that user and target user are not the same
        Long toUserId = createTransferDTO.toUserId();
        if (fromUser.getId().equals(toUserId)) {
            throw new BadRequestException("Cannot transfer to yourself");
        }

        // try to find target user by toUserId with PESSIMISTIC_WRITE lock
        UserEntity toUser = userRepository.findByIdWithAccountForUpdate(toUserId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found"));

        // check the balance, it must be positive
        if (fromUser.getAccount().getBalance().compareTo(createTransferDTO.amount()) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        // change the balances
        fromUser.getAccount().setBalance(fromUser.getAccount().getBalance().subtract(createTransferDTO.amount()));
        toUser.getAccount().setBalance(toUser.getAccount().getBalance().add(createTransferDTO.amount()));

        return new TransferDTO(fromUser.getId(), toUser.getId(), createTransferDTO.amount());
    }

}
