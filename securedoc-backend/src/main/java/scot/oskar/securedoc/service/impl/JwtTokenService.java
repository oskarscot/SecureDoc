package scot.oskar.securedoc.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;
import scot.oskar.securedoc.data.model.RefreshToken;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.AuthenticationExpiredException;
import scot.oskar.securedoc.repository.RefreshTokenRepository;
import scot.oskar.securedoc.repository.UserRepository;

@Slf4j
@Getter
@Service
public class JwtTokenService {

  private final SecretKey secretKey;
  private final UserRepository userRepository;
  private final RefreshTokenRepository tokenRepository;
  private final long accessExpiration;
  private final long refreshExpiration;

  public JwtTokenService(
      @Value("${app.jwt-secret}") String secret,
      UserRepository userRepository,
      RefreshTokenRepository tokenRepository,
      @Value("${app.jwt-access-expiration:900000}") long accessExpiration,  // Default: 15 minutes
      @Value("${app.jwt-refresh-expiration:604800000}") long refreshExpiration  // Default: 7 days
  ) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;
  }

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

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(authToken);
      return true;
    } catch (Exception ignored) { }
    return false;
  }
}
