package backend.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import backend.model.User;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtUtils {
    @Value("${spring.jwt.secret}")
    private String jwtSecret;
    private final int jwtExpirationMinutes = 60;

    public String generateAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(jwtExpirationMinutes).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getName())
                .setExpiration(accessExpiration)
                .signWith(getSigningKey())
                .claim("id", user.getId())
                .compact();
    }

    public boolean validateAccessToken(String jwtToken) {
        try {        
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken);
            return true;
        } catch (ClaimJwtException e) {
            throw e;
        }
    }

    public Claims getClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public int getIdFromToken(String token) {
        return getClaims(token).get("id", Integer.class);
    }
}
