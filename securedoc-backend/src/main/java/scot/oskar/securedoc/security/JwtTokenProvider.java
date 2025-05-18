package scot.oskar.securedoc.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private final SecretKey secretKey;
  private final long accessExpiration;  // For access tokens
  private final long refreshExpiration; // For refresh tokens

  public JwtTokenProvider(
      @Value("${app.jwtSecret}") String secret,
      @Value("${app.jwtAccessExpirationMs:900000}") long accessExpiration,  // Default: 15 minutes
      @Value("${app.jwtRefreshExpirationMs:604800000}") long refreshExpiration  // Default: 7 days
  ) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;
  }

  public String generateToken(final Authentication authentication) {
    final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    final Date now = new Date();
    final Date expiration = new Date(now.getTime() + this.accessExpiration);

    String roles = userPrincipal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .claims()
        .subject(userPrincipal.getId().toString())
        .add("username", userPrincipal.getUsername())
        .add("roles", roles)
        .and()
        .issuedAt(now)
        .expiration(expiration)
        .signWith(this.secretKey)
        .compact();
  }

  public String generateRefreshToken(final UUID uuid) {
    final Date now = new Date();
    final Date expiration = new Date(now.getTime() + this.refreshExpiration);

    return Jwts.builder()
        .subject(uuid.toString())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(this.secretKey)
        .compact();
  }

  public UUID getUserIdFromToken(final String token) {
    final Claims payload = Jwts.parser()
        .verifyWith(this.secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return UUID.fromString(payload.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(authToken);
      return true;
    } catch (SignatureException ex) {
      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty");
    }
    return false;
  }
}
