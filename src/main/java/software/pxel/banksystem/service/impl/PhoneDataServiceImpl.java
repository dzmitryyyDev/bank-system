package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreatePhoneDataDTO;
import software.pxel.banksystem.api.dto.request.UpdatePhoneDataDTO;
import software.pxel.banksystem.api.dto.response.PhoneDataDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.api.exception.AlreadyExistsException;
import software.pxel.banksystem.config.security.utils.JwtUtils;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.PhoneDataRepository;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.PhoneDataService;
import software.pxel.banksystem.service.UserService;
import software.pxel.banksystem.service.common.CommonService;
import software.pxel.banksystem.service.mapper.PhoneDataMapper;

@Service
public class PhoneDataServiceImpl implements PhoneDataService {

    private final PhoneDataMapper phoneDataMapper;

    private final UserRepository userRepository;

    private final PhoneDataRepository phoneDataRepository;

    private final JwtUtils jwtUtils;

    private final CommonService commonService;

    private final UserService userService;


    public PhoneDataServiceImpl(PhoneDataMapper phoneDataMapper, PhoneDataRepository phoneDataRepository, UserRepository userRepository, JwtUtils jwtUtils, CommonService commonService, UserService userService) {
        this.phoneDataMapper = phoneDataMapper;
        this.userRepository = userRepository;
        this.phoneDataRepository = phoneDataRepository;
        this.jwtUtils = jwtUtils;
        this.commonService = commonService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PhoneDataDTO create(CreatePhoneDataDTO request) {
        // get userId from jwt token
        Long userId = jwtUtils.getUserIdFromSecurityContext();

        // try to find user by userId
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // check that new phone is unique
        if (phoneDataRepository.existsByPhone(request.phone())) {
            throw new AlreadyExistsException("Phone already exists");
        }

        PhoneDataEntity phoneData = new PhoneDataEntity();
        phoneData.setPhone(request.phone());
        phoneData.setUser(user);

        user.getPhoneData().add(phoneData);

        // evict users cache
        userService.evictUsersCache();

        return phoneDataMapper.toDTO(phoneDataRepository.save(phoneData));
    }

    @Override
    @Transactional
    public PhoneDataDTO update(Long phoneDataId, UpdatePhoneDataDTO request) {
        PhoneDataEntity phoneData = phoneDataRepository.findById(phoneDataId)
                .orElseThrow(() -> new EntityNotFoundException("Phone data not found"));

        // get userId from jwt token and check that this user is owner of this phone
        Long userId = jwtUtils.getUserIdFromSecurityContext();
        commonService.validateOwnership(phoneData.getUser(), userId);

        if (request.phone().equals(phoneData.getPhone())) {
            return phoneDataMapper.toDTO(phoneData);
        }

        if (phoneDataRepository.existsByPhone(request.phone())) {
            throw new AlreadyExistsException("Phone already exists");
        }

        phoneData.setPhone(request.phone());

        // evict users cache
        userService.evictUsersCache();

        return phoneDataMapper.toDTO(phoneData);
    }

    @Override
    @Transactional
    public PhoneDataDTO delete(Long phoneDataId) {
        // try to find phone data by id
        PhoneDataEntity phoneData = phoneDataRepository.findById(phoneDataId)
                .orElseThrow(() -> new EntityNotFoundException("Phone data not found"));

        // get userId from jwt token and check that this user is owner of this phone
        Long userId = jwtUtils.getUserIdFromSecurityContext();
        commonService.validateOwnership(phoneData.getUser(), userId);

        UserEntity user = phoneData.getUser();

        // cannot delete last user's phone
        if (user.getPhoneData().size() <= 1) {
            throw new BadRequestException("User must have at least one phone");
        }

        user.getPhoneData().remove(phoneData);

        PhoneDataDTO dto = phoneDataMapper.toDTO(phoneData);

        phoneDataRepository.delete(phoneData);

        // evict users cache
        userService.evictUsersCache();

        return dto;
    }
}
