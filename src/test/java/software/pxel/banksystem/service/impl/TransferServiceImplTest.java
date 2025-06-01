package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.pxel.banksystem.api.dto.request.CreateTransferDTO;
import software.pxel.banksystem.api.dto.response.TransferDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.api.exception.ErrorMessages;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.AccountEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void createTransfer_SuccessfulTransfer() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        CreateTransferDTO createTransferDTO = new CreateTransferDTO(toUserId, amount);

        UserEntity fromUser = new UserEntity();
        fromUser.setId(fromUserId);
        AccountEntity fromAccount = new AccountEntity();
        fromAccount.setBalance(new BigDecimal("500.00"));
        fromUser.setAccount(fromAccount);

        UserEntity toUser = new UserEntity();
        toUser.setId(toUserId);
        AccountEntity toAccount = new AccountEntity();
        toAccount.setBalance(new BigDecimal("200.00"));
        toUser.setAccount(toAccount);

        when(jwtUtils.getUserIdFromSecurityContext()).thenReturn(fromUserId);
        when(userRepository.findByIdWithAccountForUpdate(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findByIdWithAccountForUpdate(toUserId)).thenReturn(Optional.of(toUser));

        TransferDTO result = transferService.create(createTransferDTO);

        assertNotNull(result);
        assertEquals(fromUserId, result.fromUserId());
        assertEquals(toUserId, result.toUserId());
        assertEquals(amount, result.amount());

        assertEquals(new BigDecimal("400.00"), fromUser.getAccount().getBalance());
        assertEquals(new BigDecimal("300.00"), toUser.getAccount().getBalance());

        verify(jwtUtils).getUserIdFromSecurityContext();
        verify(userRepository).findByIdWithAccountForUpdate(fromUserId);
        verify(userRepository).findByIdWithAccountForUpdate(toUserId);
    }

    @Test
    void createTransfer_FromUserNotFound_ThrowsException() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        CreateTransferDTO createTransferDTO = new CreateTransferDTO(toUserId, amount);

        when(jwtUtils.getUserIdFromSecurityContext()).thenReturn(fromUserId);
        when(userRepository.findByIdWithAccountForUpdate(fromUserId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transferService.create(createTransferDTO));
        assertEquals(ErrorMessages.NOT_FOUND, exception.getMessage());

        verify(jwtUtils).getUserIdFromSecurityContext();
        verify(userRepository).findByIdWithAccountForUpdate(fromUserId);
        verify(userRepository, never()).findByIdWithAccountForUpdate(toUserId);
    }

    @Test
    void createTransfer_ToUserNotFound_ThrowsException() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        CreateTransferDTO createTransferDTO = new CreateTransferDTO(toUserId, amount);

        UserEntity fromUser = new UserEntity();
        fromUser.setId(fromUserId);
        fromUser.setAccount(new AccountEntity());

        when(jwtUtils.getUserIdFromSecurityContext()).thenReturn(fromUserId);
        when(userRepository.findByIdWithAccountForUpdate(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findByIdWithAccountForUpdate(toUserId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transferService.create(createTransferDTO));
        assertEquals(ErrorMessages.NOT_FOUND, exception.getMessage());

        verify(jwtUtils).getUserIdFromSecurityContext();
        verify(userRepository).findByIdWithAccountForUpdate(fromUserId);
        verify(userRepository).findByIdWithAccountForUpdate(toUserId);
    }

    @Test
    void createTransfer_TransferToSelf_ThrowsException() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        CreateTransferDTO createTransferDTO = new CreateTransferDTO(userId, amount);

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setAccount(new AccountEntity());

        when(jwtUtils.getUserIdFromSecurityContext()).thenReturn(userId);
        when(userRepository.findByIdWithAccountForUpdate(userId)).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transferService.create(createTransferDTO));
        assertEquals(ErrorMessages.TRANSFER_TO_YOURSELF, exception.getMessage());

        verify(jwtUtils).getUserIdFromSecurityContext();
        verify(userRepository).findByIdWithAccountForUpdate(userId);
        verify(userRepository, never()).findByIdWithAccountForUpdate(not(eq(userId)));
    }

    @Test
    void createTransfer_InsufficientBalance_ThrowsException() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("600.00");

        CreateTransferDTO createTransferDTO = new CreateTransferDTO(toUserId, amount);

        UserEntity fromUser = new UserEntity();
        fromUser.setId(fromUserId);
        AccountEntity fromAccount = new AccountEntity();
        fromAccount.setBalance(new BigDecimal("500.00"));
        fromUser.setAccount(fromAccount);

        UserEntity toUser = new UserEntity();
        toUser.setId(toUserId);
        toUser.setAccount(new AccountEntity());

        when(jwtUtils.getUserIdFromSecurityContext()).thenReturn(fromUserId);
        when(userRepository.findByIdWithAccountForUpdate(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findByIdWithAccountForUpdate(toUserId)).thenReturn(Optional.of(toUser));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transferService.create(createTransferDTO));
        assertEquals(ErrorMessages.INSUFFICIENT_BALANCE, exception.getMessage());

        verify(jwtUtils).getUserIdFromSecurityContext();
        verify(userRepository).findByIdWithAccountForUpdate(fromUserId);
        verify(userRepository).findByIdWithAccountForUpdate(toUserId);
    }
}
