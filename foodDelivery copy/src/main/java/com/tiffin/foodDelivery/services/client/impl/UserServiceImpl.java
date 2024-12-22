package com.tiffin.foodDelivery.services.client.impl;

import com.tiffin.foodDelivery.audits.ApplicationAuditAware;
import com.tiffin.foodDelivery.dtos.client.UserDTO;
import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import com.tiffin.foodDelivery.mappers.client.UserMapper;
import com.tiffin.foodDelivery.repositories.UserRepository;
import com.tiffin.foodDelivery.security.dtos.NewUserDTO;
import com.tiffin.foodDelivery.services.client.UserService;
import com.tiffin.foodDelivery.utils.constants.LogConstants;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationAuditAware applicationAuditAware;

    @Override
    public User getUserFromRegisterDTO(NewUserDTO newUserDTO) throws BaseException {
        return userMapper.toEntityFromRegisterDTO(newUserDTO);
    }

    @Override
    public User getEntity(UserDTO userDTO) {
        return userMapper.toEntity(userDTO);
    }

    @Override
    public UserDTO getDTO(User user) {
        return userMapper.toDTO(user);
    }

    @Override
    public List<User> getEntityList(List<UserDTO> userDTOList) {
        return userMapper.toEntityList(userDTOList);
    }

    @Override
    public List<UserDTO> getDTOList(List<User> userList) {
        return userMapper.toDTOList(userList);
    }

    @Override
    @Transactional
    public User finalSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        Optional<String> currentAuditor = applicationAuditAware.getCurrentAuditor();
        if (currentAuditor.isEmpty()) {
            user.setCreatedBy("system");
            user.setLastUpdatedBy("system");
            logger.info(LogConstants.getAuditorNotFoundMessage("User", user.getEmail(), "while creating"));
        } else {
            String auditor = currentAuditor.get();
            user.setCreatedBy(auditor);
            user.setLastUpdatedBy(auditor);
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        return foundUser.orElse(null);
    }

    @Override
    public User getUserByUserId(String userId) {
        Optional<User> foundUser = userRepository.findUserById(userId);
        return foundUser.orElse(null);
    }

    @Override
    public String getUsernameByUserId(String userId) {
        if(!userRepository.existsById(userId)) {
            return "unknown";
        }
        Optional<String> foundUser = userRepository.findUsernameByUserId(userId);
        return foundUser.orElse(null);
    }

}
