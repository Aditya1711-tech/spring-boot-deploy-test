package com.tiffin.foodDelivery.services;

import com.tiffin.foodDelivery.security.dtos.AuthResponseDTO;
import com.tiffin.foodDelivery.utils.functions.CookieUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CookieService {

    private final CookieUtils cookieUtils;
    private final static String PUBLIC_PATH = "/";

    public CookieService(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public List<ResponseCookie> generateAuthCookies(AuthResponseDTO response) {
        ResponseCookie tokenCookie = cookieUtils.generateCookie("Token",
                URLEncoder.encode(response.getAccessToken(), StandardCharsets.UTF_8), PUBLIC_PATH);
        ResponseCookie userNameCookie = cookieUtils.generateCookie("User_Name",
                URLEncoder.encode(response.getUser().getName(), StandardCharsets.UTF_8), PUBLIC_PATH);
        ResponseCookie userIdCookie = cookieUtils.generateCookie("User_Id",
                URLEncoder.encode(String.valueOf(response.getUser().getId()), StandardCharsets.UTF_8), PUBLIC_PATH);
        ResponseCookie roleCookie = cookieUtils.generateCookie("User_Role",
                URLEncoder.encode(response.getUser().getRoles(), StandardCharsets.UTF_8), PUBLIC_PATH);
        ResponseCookie permissionCookie = cookieUtils.generateCookie("User_Permission",
                URLEncoder.encode(String.join("-", response.getUser().getPermissions()), StandardCharsets.UTF_8), PUBLIC_PATH);

        return List.of(tokenCookie, userNameCookie, userIdCookie, roleCookie, permissionCookie);
    }


    public List<ResponseCookie> clearAuthCookies(){
        ResponseCookie tokenCookie = cookieUtils.clearCookie("Token", PUBLIC_PATH);
        ResponseCookie userIdCookie = cookieUtils.clearCookie("User_Id", PUBLIC_PATH);
        ResponseCookie userNameCookie = cookieUtils.clearCookie("User_Name", PUBLIC_PATH);
        ResponseCookie roleCookie = cookieUtils.clearCookie("User_Role", PUBLIC_PATH);
        ResponseCookie permissionCookie = cookieUtils.clearCookie("User_Permission", PUBLIC_PATH);

        return List.of(tokenCookie, roleCookie, permissionCookie);
    }
}

