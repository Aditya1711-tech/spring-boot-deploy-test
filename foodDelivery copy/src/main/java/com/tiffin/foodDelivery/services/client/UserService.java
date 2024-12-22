package com.tiffin.foodDelivery.services.client;

import com.tiffin.foodDelivery.dtos.client.UserDTO;
import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import com.tiffin.foodDelivery.security.dtos.NewUserDTO;

import java.util.List;

public interface UserService {

    // User mapper
    User getUserFromRegisterDTO(NewUserDTO newUserDTO) throws BaseException;
    User getEntity(UserDTO userDTO);
    UserDTO getDTO(User user);
    List<User> getEntityList(List<UserDTO> userDTOList);
    List<UserDTO> getDTOList(List<User> userList);

    // User Repository
    User finalSave(User user);
    User getUserByEmail(String email);
    User getUserByUserId(String userId);
    String getUsernameByUserId(String userId);
}
