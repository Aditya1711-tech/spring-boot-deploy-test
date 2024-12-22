package com.tiffin.foodDelivery.security.services;

import com.tiffin.foodDelivery.dtos.client.UserDTO;
import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.client.LoginFailedException;
import com.tiffin.foodDelivery.exceptions.client.RegistrationFailedException;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import com.tiffin.foodDelivery.repositories.UserRepository;
import com.tiffin.foodDelivery.security.LoggedUserContext;
import com.tiffin.foodDelivery.security.dtos.AuthRequestDTO;
import com.tiffin.foodDelivery.security.dtos.AuthResponseDTO;
import com.tiffin.foodDelivery.security.dtos.NewUserDTO;
import com.tiffin.foodDelivery.services.client.UserService;
import com.tiffin.foodDelivery.utils.constants.LogConstants;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import com.tiffin.foodDelivery.utils.functions.MessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final UserService userService;
//    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public UserDTO addNewUser(NewUserDTO newUserDTO) {
        try {
            var user = userService.getUserFromRegisterDTO(newUserDTO);
            var savedUser = userService.finalSave(user);

//            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());

            logger.info(LogConstants.getCreatedSuccessfullyMessage("User", "DTO", newUserDTO, "new user"));
            return userService.getDTO(savedUser);
        } catch (RegistrationFailedException e) {
            logger.error(LogConstants.getAlreadyExistsMessage("User", "Email", newUserDTO.getEmail(), "while registering"));
            throw new RegistrationFailedException(MessageConstants.UserError.EMAIL_EXISTS);
        } catch (BaseException e) {
            logger.error("Did Not Match: Attempt to create a user with password: {} and confirmPassword: {}", newUserDTO.getPassword(), newUserDTO.getConfirmPassword());
            throw new RegistrationFailedException(MessageConstants.UserError.CONFIRM_PASSWORD_DID_NOT_MATCH);
        }
    }


    public AuthResponseDTO authenticate(AuthRequestDTO request, String moduleType) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error(LogConstants.getNotFoundMessage("User", "login", "Email", request.getEmail(), "while authenticating"));
                    logger.error(LogConstants.getLogInFailedMessage(request.getEmail(), null, "User Account Not Found"));
                    return new LoginFailedException(MessageConstants.UserError.USER_NOT_FOUND);
                });

        if (!user.isAccountEnabled()) {
            logger.error(LogConstants.getAccountActionTakenMessage(user.getEmail(), "Disabled", user.getId(), "while authenticating"));
            logger.error(LogConstants.getLogInFailedMessage(user.getEmail(), user.getId(), "User Account Is Disabled"));
            throw new LoginFailedException(MessageConstants.UserError.ACCOUNT_DISABLED);
        } else if (!user.isAccountNonLocked()) {
            logger.error(LogConstants.getAccountActionTakenMessage(user.getEmail(), "Locked", user.getId(), "while authenticating"));
            logger.error(LogConstants.getLogInFailedMessage(user.getEmail(), user.getId(), "User Account Is Locked"));
            throw new LoginFailedException(MessageConstants.UserError.ACCOUNT_LOCKED);
        }

        if (user.getLoginAttempts() >= 3) {
            user.setAccountNonLocked(false);
            user.setLoginAttempts(0);
            userRepository.save(user);
            logger.error(LogConstants.getAccountActionTakenMessage(user.getEmail(), "Locked", user.getId(), null));
            logger.error(LogConstants.getLogInFailedMessage(user.getEmail(), user.getId(), "Max Login Attempts Reached"));
            throw new LoginFailedException(MessageConstants.UserError.ACCOUNT_LOCKED);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            userRepository.save(user);
            logger.error(LogConstants.getLogInFailedMessage(user.getEmail(), user.getId(), "Invalid Credentials"));
            throw new LoginFailedException(MessageConstants.UserError.INVALID_CREDENTIALS);
        }

        user.setLoginAttempts(0);
        User loggedUser = userRepository.save(user);

        // token generation
        var jwtToken = jwtService.generateTokenForUser(user, user.getEmail(), moduleType);
        var refreshToken = jwtService.generateRefreshTokenForUser(user, user.getEmail(), moduleType);
        LoggedUserContext.setCurrentUser(loggedUser);

        logger.info(LogConstants.getLoggedInSuccessMessage(loggedUser.getEmail(), loggedUser.getId()));
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userService.getDTO(user))
                .build();
    }

    public AuthResponseDTO refreshToken(String refreshToken) {

        // Extract email from the refresh token
        String email = jwtService.extractEmail(refreshToken);

        // Load the user details using the email extracted from the token
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Validate the refresh token
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            logger.error("Invalid or expired refresh token for user: {}", email);
            throw new LoginFailedException(MessageConstants.JWT.EXPIRED_JWT_EXCEPTION);
        }

        // Fetch the user from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailedException(MessageConstants.UserError.USER_NOT_FOUND));

        // Generate a new access token
        String newAccessToken = jwtService.generateTokenForUser(user, email, "HR");

        logger.info("Refresh token successfully used for user with email: {}", email);

        // Return the new tokens in the response
        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .user(userService.getDTO(user))
                .build();
    }

    public boolean isTokenValid(String token, String userId) {
        User loggedUser = userRepository.findByEmail(jwtService.extractEmail(token)).orElseThrow(
                () -> {
                    logger.error(MessageCreator.getNotFoundMessage("User"));
                    return new RuntimeException(MessageConstants.UserError.USER_NOT_FOUND);
                }
        );
        return !jwtService.isTokenExpired(token) && String.valueOf(loggedUser.getId()).equals(userId);
    }

}
