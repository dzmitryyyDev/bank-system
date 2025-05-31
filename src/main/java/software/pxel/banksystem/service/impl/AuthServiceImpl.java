package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.pxel.banksystem.api.dto.request.LoginRequestDTO;
import software.pxel.banksystem.api.dto.response.LoginResponseDTO;
import software.pxel.banksystem.api.exception.UnauthorizedException;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.EmailDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.EmailDataRepository;
import software.pxel.banksystem.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final EmailDataRepository emailDataRepository;

    public AuthServiceImpl(EmailDataRepository emailDataRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.emailDataRepository = emailDataRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // try to find email data
        EmailDataEntity emailData = emailDataRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // get user from email data and compare passwords
        UserEntity user = emailData.getUser();
        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new UnauthorizedException("Incorrect password");
        }

        // generate jwt token
        String token = jwtUtils.generateToken(user.getId());

        return new LoginResponseDTO(token);
    }

}
