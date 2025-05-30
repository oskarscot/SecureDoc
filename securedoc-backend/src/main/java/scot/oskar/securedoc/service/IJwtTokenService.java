package scot.oskar.securedoc.service;

import java.util.UUID;
import scot.oskar.securedoc.data.model.RefreshToken;

public interface IJwtTokenService {

  String generateAccessToken(UUID principalId);

  RefreshToken generateRefreshToken(UUID principalId);

  boolean validateToken(String authToken);
}
