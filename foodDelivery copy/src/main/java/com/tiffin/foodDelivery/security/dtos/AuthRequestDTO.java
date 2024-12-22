package com.tiffin.foodDelivery.security.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthRequestDTO {
    private String email;
    private String password;
}
