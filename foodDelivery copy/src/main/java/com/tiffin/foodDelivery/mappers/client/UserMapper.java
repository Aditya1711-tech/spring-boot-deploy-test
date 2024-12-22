package com.tiffin.foodDelivery.mappers.client;

import com.tiffin.foodDelivery.dtos.client.UserDTO;
import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.client.LoginFailedException;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import com.tiffin.foodDelivery.security.dtos.NewUserDTO;
import com.tiffin.foodDelivery.services.client.UserService;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import com.tiffin.foodDelivery.utils.functions.CustomDateTimeFormatter;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserMapper {

    @Lazy
    @Autowired
    private UserService userService;

    @Named("toDTO")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "isAccountEnabled", ignore = true)
    @Mapping(target = "isCredentialsNonExpired", ignore = true)
    @Mapping(target = "isAccountNonLocked", ignore = true)
    @Mapping(target = "isAccountNonExpired", ignore = true)
    public abstract UserDTO toDTO(User user);

    @Named("toEntity")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract User toEntity(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdatedDate", ignore = true)
    @Mapping(target = "lastUpdatedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract User toEntityFromRegisterDTO(NewUserDTO newUserDTO);

    public abstract List<User> toEntityList(List<UserDTO> userDTOList);

    public abstract List<UserDTO> toDTOList(List<User> userList);

    @BeforeMapping
    protected void validatePassword(NewUserDTO newUserDTO, @MappingTarget User user) throws BaseException {
        if (!isEmailExists(newUserDTO.getEmail())) {
            throw new LoginFailedException(MessageConstants.UserError.EMAIL_EXISTS);
        }

        if (!newUserDTO.getPassword().equals(newUserDTO.getConfirmPassword())) {
            throw new BaseException(MessageConstants.UserError.CONFIRM_PASSWORD_DID_NOT_MATCH);
        }
    }

    private boolean isEmailExists(String email) {
        User user = userService.getUserByEmail(email);
        return ObjectUtils.isEmpty(user);
    }

    @AfterMapping
    protected void stringFormatting(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setRoles(user.getRoles().getLabel());
        userDTO.setPermissions(user.getRoles().getPermissionStrings());
        userDTO.setIsAccountEnabled(this.setBooleanString(user.isAccountEnabled()));
        userDTO.setIsAccountNonExpired(this.setBooleanString(user.isAccountNonExpired()));
        userDTO.setIsAccountNonLocked(this.setBooleanString(user.isAccountNonLocked()));
        userDTO.setIsCredentialsNonExpired(this.setBooleanString(user.isCredentialsNonExpired()));
        userDTO.setCreatedDate(CustomDateTimeFormatter.getFormatedDateTimeByIntensity(user.getCreatedDate()));
        userDTO.setLastUpdatedDate(CustomDateTimeFormatter.getFormatedDateTimeByIntensity(user.getLastUpdatedDate()));
        userDTO.setLastUpdatedBy(userService.getUsernameByUserId(user.getId()));
    }

    private String setBooleanString(Boolean bool) {
        return bool ? "true" : "false";
    }

}
