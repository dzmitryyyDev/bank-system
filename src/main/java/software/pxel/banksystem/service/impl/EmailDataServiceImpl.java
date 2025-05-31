package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreateEmailDataDTO;
import software.pxel.banksystem.api.dto.request.UpdateEmailDataDTO;
import software.pxel.banksystem.api.dto.response.EmailDataDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.api.exception.AlreadyExistsException;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.EmailDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.EmailDataRepository;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.EmailDataService;
import software.pxel.banksystem.service.common.CommonService;
import software.pxel.banksystem.service.mapper.EmailDataMapper;

@Service
public class EmailDataServiceImpl implements EmailDataService {

    private final EmailDataMapper emailDataMapper;

    private final EmailDataRepository emailDataRepository;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final CommonService commonService;


    public EmailDataServiceImpl(EmailDataRepository emailDataRepository, EmailDataMapper emailDataMapper, UserRepository userRepository, JwtUtils jwtUtils, CommonService commonService) {
        this.emailDataRepository = emailDataRepository;
        this.emailDataMapper = emailDataMapper;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.commonService = commonService;
    }

    @Override
    @Transactional
    public EmailDataDTO create(CreateEmailDataDTO request) {
        // get userId from jwt token
        Long userId = jwtUtils.getUserIdFromSecurityContext();

        // try to find user by userId
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // check that new email is unique
        if (emailDataRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("Email already exists");
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
        // try to find email data by id
        EmailDataEntity emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        // get userId from jwt token and check that this user is owner of this email
        Long userId = jwtUtils.getUserIdFromSecurityContext();
        commonService.validateOwnership(emailData.getUser(), userId);

        // no changes needed if the new email is the same as the current one
        if (request.email().equals(emailData.getEmail())) {
            return emailDataMapper.toDTO(emailData);
        }

        // check that new email is unique
        if (emailDataRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("Email already exists");
        }

        emailData.setEmail(request.email());

        return emailDataMapper.toDTO(emailData);
    }

    @Override
    @Transactional
    public EmailDataDTO delete(Long emailId) {
        // try to find email data by id
        EmailDataEntity emailData = emailDataRepository.findById(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        // get userId from jwt token and check that this user is owner of this email
        Long userId = jwtUtils.getUserIdFromSecurityContext();
        commonService.validateOwnership(emailData.getUser(), userId);

        UserEntity user = emailData.getUser();

        // cannot delete last user's email
        if (user.getEmailData().size() <= 1) {
            throw new BadRequestException("User must have at least one email");
        }

        user.getEmailData().remove(emailData);

        EmailDataDTO dto = emailDataMapper.toDTO(emailData);

        emailDataRepository.delete(emailData);

        return dto;
    }
}
