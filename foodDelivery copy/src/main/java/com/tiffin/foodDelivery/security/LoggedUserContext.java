package com.tiffin.foodDelivery.security;

import com.tiffin.foodDelivery.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoggedUserContext {
    private static final Logger logger = LoggerFactory.getLogger(LoggedUserContext.class);

    private static final ThreadLocal<User> loggedUser = ThreadLocal.withInitial(() -> null);

    public static User getCurrentUser() {
        return LoggedUserContext.loggedUser.get();
    }

    public static synchronized void setCurrentUser(User user) {
        LoggedUserContext.loggedUser.set(user);
    }

    public static void clear() {
        LoggedUserContext.loggedUser.remove();
    }

    @SuppressWarnings(value = "unused")
    public static boolean isLoggedUser(Long userId) {
        User currentUser = getCurrentUser();
        if(currentUser != null) {
            if(Objects.equals(currentUser.getId(), userId)) {
                return true;
            } else {
                logger.warn("User with Id: {} tried to access user with id: {}", userId, currentUser.getId());
                return false;
            }
        }
        return false;
    }
}
