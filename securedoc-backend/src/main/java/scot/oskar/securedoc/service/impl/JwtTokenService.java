package scot.oskar.securedoc.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Optional;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import scot.oskar.securedoc.data.model.RefreshToken;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.AuthenticationExpiredException;
import scot.oskar.securedoc.repository.RefreshTokenRepository;
import scot.oskar.securedoc.repository.UserRepository;
import scot.oskar.securedoc.service.IJwtTokenService;

@Getter
@Component
public class JwtTokenService implements IJwtTokenService {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

  private final SecretKey secretKey;
  private final UserRepository userRepository;
  private final RefreshTokenRepository tokenRepository;
  private final long accessExpiration;  // For access tokens
  private final long refreshExpiration; // For refresh tokens

  public JwtTokenService(
      @Value("${app.jwtSecret}") String secret,
      UserRepository userRepository,
      RefreshTokenRepository tokenRepository,
      @Value("${app.jwtAccessExpirationMs:900000}") long accessExpiration,  // Default: 15 minutes
      @Value("${app.jwtRefreshExpirationMs:604800000}") long refreshExpiration  // Default: 7 days
  ) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;
  }

  @Override
  public String generateAccessToken(UUID principalId) {
    final Date now = new Date();
    final Date expiration = new Date(now.getTime() + this.accessExpiration);

    final User user = userRepository.findById(principalId)
        .orElseThrow(() -> new RuntimeException("Cannot generate token for invalid user id.")); // should never happen

    return Jwts.builder()
        .claims()
        .subject(user.getId().toString())
        .add("username", user.getUsername())
        .and()
        .issuedAt(now)
        .expiration(expiration)
        .signWith(this.secretKey)
        .compact();
  }

  @Override
  public RefreshToken generateRefreshToken(UUID principalId) {
    final Date now = new Date();
    final Date expiration = new Date(now.getTime() + this.refreshExpiration);

    final User user = userRepository.findById(principalId)
        .orElseThrow(() -> new RuntimeException("Cannot generate token for invalid user id.")); // should never happen

    final String jwtToken = Jwts.builder()
        .subject(user.getId().toString())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(this.secretKey)
        .compact();

    final RefreshToken refreshToken = RefreshToken.builder()
        .token(jwtToken)
        .expiry(expiration.toInstant())
        .user(user)
        .build();

    tokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token){
    if(token.getExpiry().compareTo(Instant.now()) < 0) {
      tokenRepository.delete(token);
      throw new AuthenticationExpiredException("Provided token has expired");
    }
    return token;
  }

  public void deactivateToken(RefreshToken token) {
    tokenRepository.delete(token);
  }

  public Optional<RefreshToken> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public Optional<RefreshToken> findByUser(User user) {
    return tokenRepository.findByUser(user);
  }

  public UUID getUserIdFromToken(final String token) {
    final Claims payload = Jwts.parser()
        .verifyWith(this.secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    return UUID.fromString(payload.getSubject());
  }

  //TODO this is ugly, clean it up
  @Override
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
