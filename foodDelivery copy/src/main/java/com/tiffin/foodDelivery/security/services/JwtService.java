package com.tiffin.foodDelivery.security.services;
import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.exceptions.jwt.JwtAuthenticationException;
import com.tiffin.foodDelivery.repositories.UserRepository;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.token-expiration-time.user}")
    private long jwtExpirationUser;
    @Value("${jwt.token-expiration-time.admin}")
    private long jwtExpirationAdmin;
    @Value("${jwt.refresh-token-expiration-time.user}")
    private long refreshExpirationUser;
    @Value("${jwt.refresh-token-expiration-time.admin}")
    private long refreshExpirationAdmin;

    private final UserRepository userRepository;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    //    CREATE TOKEN FOR USER
    public String generateTokenForUser(UserDetails userDetails, String subject, String moduleType) {
        return generateTokenForUser(new HashMap<>(), userDetails, subject, moduleType);
    }

    //    TOKEN BUILDER FOR USER
    public String generateTokenForUser(Map<String, Object> extraClaims, UserDetails userDetails, String subject, String moduleType) {
        return buildToken(extraClaims, userDetails, jwtExpirationUser, subject, moduleType);
    }


    //    CREATE TOKEN FOR ADMIN
    public String generateTokenForAdmin(UserDetails userDetails, String subject, String moduleType) {
        return generateTokenForAdmin(new HashMap<>(), userDetails, subject, moduleType);
    }

    //    TOKEN BUILDER FOR ADMIN
    public String generateTokenForAdmin(Map<String, Object> extraClaims, UserDetails userDetails, String subject, String moduleType) {
        return buildToken(extraClaims, userDetails, jwtExpirationAdmin, subject, moduleType);
    }


    //    REFRESH TOKEN BUILDER FOR USER
    public String generateRefreshTokenForUser(UserDetails userDetails, String subject, String moduleType) {
        Map<String, Object> rolesMap = new HashMap<>(1);
        Optional<User> tokenUser = userRepository.findByEmail(userDetails.getUsername());
        tokenUser.ifPresent(user -> rolesMap.put("roles", user.getRoles().getPermissions()));
        return buildToken(rolesMap, userDetails, refreshExpirationUser, subject, moduleType);
    }

    //    REFRESH TOKEN BUILDER FOR ADMIN
    public String generateRefreshTokenForAdmin(UserDetails userDetails, String subject, String moduleType) {
        Map<String, Object> rolesMap = new HashMap<>(1);
        Optional<User> tokenUser = userRepository.findByEmail(userDetails.getUsername());
        tokenUser.ifPresent(user -> rolesMap.put("roles", user.getRoles().getPermissions()));
        return buildToken(rolesMap, userDetails, refreshExpirationAdmin, subject, moduleType);
    }


    // TOKEN BUILDER METHOD
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, String subject, String moduleType) throws InvalidKeyException {
        return Jwts
                .builder()
                .claims(extraClaims)
                .header().add(getHeader(moduleType)).and()
                .subject(subject != null ? subject : userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())   // By default, it will use HS512. If you want to use any other, add 'SignatureAlgorithm.X' as second parameter.
                .compact();
    }


    // CHECK IF TOKEN IS VALID
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);

        if (!username.equals(userDetails.getUsername())) {
            throw new JwtAuthenticationException(MessageConstants.JWT.INVALID_TOKEN);
        }

        if (isTokenExpired(token)) {
            throw new JwtAuthenticationException(MessageConstants.JWT.EXPIRED_JWT_EXCEPTION);
        }

        return true;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith((SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SecurityException e) {
            throw new RuntimeException(MessageConstants.JWT.SECURITY_EXCEPTION, e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(MessageConstants.JWT.MALFORMED_JWT_EXCEPTION, e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(MessageConstants.JWT.EXPIRED_JWT_EXCEPTION, e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(MessageConstants.JWT.UNSUPPORTED_JWT_EXCEPTION, e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(MessageConstants.JWT.ILLEGAL_ARGUMENT_EXCEPTION, e);
        }
    }


    // GETTING THE JWT HEADER
    private Map<String, Object> getHeader(String moduleType) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS512"); // Specifying the algorithm
        headerMap.put("typ", "JWT"); // Token type
        headerMap.put("creator", moduleType.toUpperCase().trim());
        return headerMap;
    }


    // GETTING THE SIGNING KEY
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
