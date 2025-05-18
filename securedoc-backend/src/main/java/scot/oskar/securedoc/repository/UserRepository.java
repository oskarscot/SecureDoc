package scot.oskar.securedoc.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import scot.oskar.securedoc.data.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsernameOrEmail(String username, String email);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
}
