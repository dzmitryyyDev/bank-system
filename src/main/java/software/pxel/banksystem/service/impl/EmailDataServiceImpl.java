package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreateEmailDataDTO;
import software.pxel.banksystem.api.dto.request.UpdateEmailDataDTO;
import software.pxel.banksystem.api.dto.response.EmailDataDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.api.exception.AlreadyExistsException;
import software.pxel.banksystem.dao.entity.EmailDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.EmailDataRepository;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.EmailDataService;
import software.pxel.banksystem.service.mapper.EmailDataMapper;

@Service
public class EmailDataServiceImpl implements EmailDataService {

    private final EmailDataMapper emailDataMapper;

    private final EmailDataRepository emailDataRepository;

    private final UserRepository userRepository;


    public EmailDataServiceImpl(EmailDataRepository emailDataRepository, EmailDataMapper emailDataMapper, UserRepository userRepository) {
        this.emailDataRepository = emailDataRepository;
        this.emailDataMapper = emailDataMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public EmailDataDTO create(Long userId, CreateEmailDataDTO request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));

        if (emailDataRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException(String.format("Email %s already exists", request.email()));
        }

        EmailDataEntity emailData = new EmailDataEntity();
        emailData.setEmail(request.email());
        emailData.setUser(user);

        user.getEmailData().add(emailData);

        return emailDataMapper.toDTO(emailDataRepository.save(emailData));
    }

    @Override
    @Transactional
    public EmailDataDTO update(Long emailId, UpdateEmailDataDTO request) {
        EmailDataEntity emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Email with id %s not found", emailId)));

        if (request.email().equals(emailData.getEmail())) {
            return emailDataMapper.toDTO(emailData);
        }

        if (emailDataRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException(String.format("Email already exists: %s", request.email()));
        }

        emailData.setEmail(request.email());

        return emailDataMapper.toDTO(emailData);
    }

    @Override
    @Transactional
    public EmailDataDTO delete(Long emailId) {
        EmailDataEntity emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Email with id %s not found", emailId)));

        UserEntity user = emailData.getUser();

        if (user.getEmailData().size() <= 1) {
            throw new BadRequestException(String.format("User with id %s must have at least one email", user.getId()));
        }

        user.getEmailData().remove(emailData);

        EmailDataDTO dto = emailDataMapper.toDTO(emailData);

        emailDataRepository.delete(emailData);

        return dto;
    }
}
