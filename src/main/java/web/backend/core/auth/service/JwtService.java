package web.backend.core.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000; // 1 giờ
    // private final long ACCESS_TOKEN_EXPIRATION = 10000; // 10 giây
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    // private final long REFRESH_TOKEN_EXPIRATION = 15000; // 7 ngày

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // public String generateToken(String username, List<String> roles, String
    // unitCode) {
    // String token = Jwts.builder()
    // .setSubject(username)
    // .claim("roles", roles)
    // .claim("unitcode", unitCode)
    // .setIssuedAt(new Date())
    // .setExpiration(new Date(System.currentTimeMillis() +
    // ACCESS_TOKEN_EXPIRATION))
    // .signWith(getSignKey(), SignatureAlgorithm.HS256)
    // .compact();
    // return token;
    // }

    public String generateAccessToken(String username, List<String> roles, String unitCode) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("unitcode", unitCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String username, String unitCode) {
        return generateAccessToken(username, Collections.emptyList(), unitCode);
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("roles", List.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot extract roles from token: " + e.getMessage());
        }
    }

    public String extractUnitCode(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("unitcode", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot extract unitCode from token: " + e.getMessage());
        }
    }

    public boolean isValidToken(String token) {

        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            return false;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            logger.debug("Token claims: {}", claims);
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void blacklistToken(String token, String username) {
        tokenBlacklistService.blacklistToken(token, username);
    }
}
