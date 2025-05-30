package software.pxel.banksystem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.pxel.banksystem.api.dto.response.UserDTO;
import software.pxel.banksystem.dao.entity.UserEntity;
import software.pxel.banksystem.dao.repository.UserRepository;
import software.pxel.banksystem.dao.repository.specification.UserSpecification;
import software.pxel.banksystem.service.UserService;
import software.pxel.banksystem.service.mapper.UserMapper;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.dateOfBirthAfter(dateOfBirth)
                .and(UserSpecification.phoneEquals(phone))
                .and(UserSpecification.nameStartsWith(name))
                .and(UserSpecification.emailEquals(email));

        return userRepository.findAll(spec, pageable).map(userMapper::toDTO);
    }
}
