package com.tiffin.foodDelivery.utils.functions;
import com.tiffin.foodDelivery.security.LoggedUserContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtils {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    @Value("${jwt.token-expiration-time.user}")
    private long jwtExpirationUser;

    public ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .maxAge(jwtExpirationUser/1000)
                .httpOnly(true)
                .sameSite("Strict")
                .build();

        logger.debug("Cookie [{}] generated for user with id: {}", cookie, LoggedUserContext.getCurrentUser().getId());

        return cookie;
    }

    public String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            logger.info("Cookie: Found cookie with name: {} and value: {}", name, cookie.getValue());
            return cookie.getValue();
        } else {
            logger.warn("Cookie: No cookie found with name: {}", name);
            return null;
        }
    }

    // Method to clear a cookie by name
    public ResponseCookie clearCookie(String name, String path) {
        ResponseCookie clearedCookie = ResponseCookie.from(name, "")
                .path(path)
                .maxAge(0)
                .httpOnly(true)
                .sameSite("Strict")
                .build();
        logger.info("Cookie: Cleared cookie with name: {} and path: {}", name, path);
        return clearedCookie;
    }
}
