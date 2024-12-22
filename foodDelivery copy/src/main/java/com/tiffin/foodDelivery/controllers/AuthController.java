package com.tiffin.foodDelivery.controllers;

import com.tiffin.foodDelivery.dtos.common.ResponseDTO;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import com.tiffin.foodDelivery.security.dtos.AuthRequestDTO;
import com.tiffin.foodDelivery.security.dtos.AuthResponseDTO;
import com.tiffin.foodDelivery.security.services.AuthenticationService;
import com.tiffin.foodDelivery.services.CookieService;
import com.tiffin.foodDelivery.utils.constants.LogConstants;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import com.tiffin.foodDelivery.utils.functions.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;
    private final CookieUtils cookieUtils;
    private final CookieService cookieService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDTO<AuthResponseDTO>> loginUser(@RequestBody AuthRequestDTO authRequestDTO) throws BaseException {
        AuthResponseDTO response = authenticationService.authenticate(authRequestDTO, "HR");

        // Generate cookies
        List<ResponseCookie> cookies = cookieService.generateAuthCookies(response);

        // Build response
        HttpHeaders headers = new HttpHeaders();
        cookies.forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));

        // Send response with data in headers and success message in body
        return ResponseEntity.ok()
                .headers(headers)
                .body(ResponseDTO.success(MessageConstants.UserSuccess.LOGIN_SUCCESS));
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<ResponseDTO<AuthResponseDTO>> generateUserRefreshToken(@RequestParam("refresh-token") String refreshToken) {
        AuthResponseDTO response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(ResponseDTO.success(MessageConstants.UserSuccess.TOKEN_REFRESHED, response));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseDTO<Void>> logoutUser() {
        // Clear cookies
        List<ResponseCookie> cookies = cookieService.clearAuthCookies();

        // Build response
        HttpHeaders headers = new HttpHeaders();
        cookies.forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));

        // Send response with data in headers and success message in body
        return ResponseEntity.ok()
                .headers(headers)
                .body(ResponseDTO.success(MessageConstants.UserSuccess.LOGOUT_SUCCESS));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ResponseDTO<Boolean>> validateUserToken(HttpServletRequest request) {
        String token = cookieUtils.getCookieValueByName(request, "Token");
        String userId = cookieUtils.getCookieValueByName(request, "User_Id");

        if (token != null && !token.isEmpty() && authenticationService.isTokenValid(token, userId)) {
            logger.info(LogConstants.getSessionVerifiedForToken(token, true));
            return ResponseEntity.ok(ResponseDTO.success(MessageConstants.UserSuccess.USER_SESSION_VERIFIED, true));
        }
        logger.error(LogConstants.getSessionVerifiedForToken(token, false));
        return ResponseEntity.badRequest().body(ResponseDTO.error(MessageConstants.UserError.USER_NOT_LOGGED_IN, false));
    }

}
