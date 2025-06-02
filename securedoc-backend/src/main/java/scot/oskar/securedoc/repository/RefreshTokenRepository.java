package scot.oskar.securedoc.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scot.oskar.securedoc.data.model.RefreshToken;
import scot.oskar.securedoc.data.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  Optional<RefreshToken> findByToken(String token);
  Optional<RefreshToken> findByUser(User user);

  Optional<RefreshToken> findByUserId(UUID userId);

}
