package com.tiffin.foodDelivery.security.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NewUserDTO {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String roles;
    private String registerModule;
}
