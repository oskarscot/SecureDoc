package scot.oskar.securedoc.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import scot.oskar.securedoc.data.model.ApiKey;
import scot.oskar.securedoc.data.model.User;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

  Optional<ApiKey> findByKey(String apiKey);
  Optional<ApiKey> findByOwner(User user);
  Optional<ApiKey> findByOwnerId(UUID uuid);

}
