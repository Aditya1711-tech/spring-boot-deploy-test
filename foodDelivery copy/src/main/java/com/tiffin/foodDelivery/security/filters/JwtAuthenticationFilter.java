package com.tiffin.foodDelivery.security.filters;

import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.jwt.JwtAuthenticationException;
import com.tiffin.foodDelivery.security.LoggedUserContext;
import com.tiffin.foodDelivery.security.services.JwtService;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import com.tiffin.foodDelivery.utils.functions.CookieUtils;
import com.tiffin.foodDelivery.utils.functions.CustomDateTimeFormatter;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String[] PUBLIC_URLS = {
            "/user/login",
            "/user/logout",
            "/user/validate-token",
            "/health/health-test"
    };


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {

            logger.info("Request remote address: {} | Time: {}", request.getRemoteAddr(), CustomDateTimeFormatter.getLocalDateTimeString(LocalDateTime.now()));

            // Check if the request is for a public URL
            String servletPath = request.getServletPath();
            for (String url : PUBLIC_URLS) {
                if (servletPath.equals(url)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String jwtToken = cookieUtils.getCookieValueByName(request, "Token");
            final String userEmail;
            if (jwtToken == null || jwtToken.isEmpty()) {
                filterChain.doFilter(request, response);
                throw new JwtAuthenticationException("Authorization token missing");
            }
            logger.info("JWT: Token found with value - {}", jwtToken);
            userEmail = jwtService.extractEmail(jwtToken);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    LoggedUserContext.setCurrentUser((User) userDetails);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.error("JWT: Token is expired");
            throw new JwtAuthenticationException(MessageConstants.JWT.EXPIRED_JWT_EXCEPTION, "403");
        } catch (JwtAuthenticationException e) {
            logger.error("JWT: Token not found");
            throw new JwtAuthenticationException(MessageConstants.UserError.USER_NOT_LOGGED_IN, "401");
        } catch (Exception e) {
            throw new RuntimeException(MessageConstants.UserError.AUTHORIZATION_FAILED);
        } finally {
            LoggedUserContext.clear();
        }
    }
}
