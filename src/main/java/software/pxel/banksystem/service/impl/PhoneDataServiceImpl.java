package software.pxel.banksystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.request.CreatePhoneDataDTO;
import software.pxel.banksystem.api.dto.request.UpdatePhoneDataDTO;
import software.pxel.banksystem.api.dto.response.PhoneDataDTO;
import software.pxel.banksystem.api.exception.BadRequestException;
import software.pxel.banksystem.api.exception.AlreadyExistsException;
import software.pxel.banksystem.dao.entity.PhoneDataEntity;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.PhoneDataRepository;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.service.PhoneDataService;
import software.pxel.banksystem.service.mapper.PhoneDataMapper;

@Service
public class PhoneDataServiceImpl implements PhoneDataService {

    private final PhoneDataMapper phoneDataMapper;

    private final UserRepository userRepository;

    private final PhoneDataRepository phoneDataRepository;


    public PhoneDataServiceImpl(PhoneDataMapper phoneDataMapper, PhoneDataRepository phoneDataRepository, UserRepository userRepository) {
        this.phoneDataMapper = phoneDataMapper;
        this.userRepository = userRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    @Override
    @Transactional
    public PhoneDataDTO create(Long userId, CreatePhoneDataDTO request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not found", userId)));

        if (phoneDataRepository.existsByPhone(request.phone())) {
            throw new AlreadyExistsException(String.format("Phone %s already exists", request.phone()));
        }

        PhoneDataEntity phoneData = new PhoneDataEntity();
        phoneData.setPhone(request.phone());
        phoneData.setUser(user);

        user.getPhoneData().add(phoneData);

        return phoneDataMapper.toDTO(phoneDataRepository.save(phoneData));
    }

    @Override
    @Transactional
    public PhoneDataDTO update(Long phoneDataId, UpdatePhoneDataDTO request) {
        PhoneDataEntity phoneData = phoneDataRepository.findById(phoneDataId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Phone data with id %s not found", phoneDataId)));

        if (request.phone().equals(phoneData.getPhone())) {
            return phoneDataMapper.toDTO(phoneData);
        }

        if (phoneDataRepository.existsByPhone(request.phone())) {
            throw new AlreadyExistsException(String.format("Phone already exists: %s", request.phone()));
        }

        phoneData.setPhone(request.phone());

        return phoneDataMapper.toDTO(phoneData);
    }

    @Override
    @Transactional
    public PhoneDataDTO delete(Long phoneDataId) {
        PhoneDataEntity phoneData = phoneDataRepository.findById(phoneDataId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Phone data with id %s not found", phoneDataId)));

        UserEntity user = phoneData.getUser();

        if (user.getPhoneData().size() <= 1) {
            throw new BadRequestException(String.format("User with id %s must have at least one phone", user.getId()));
        }

        user.getPhoneData().remove(phoneData);

        PhoneDataDTO dto = phoneDataMapper.toDTO(phoneData);

        phoneDataRepository.delete(phoneData);

        return dto;
    }
}
