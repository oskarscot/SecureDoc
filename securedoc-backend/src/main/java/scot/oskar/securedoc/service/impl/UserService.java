package scot.oskar.securedoc.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> retrieveById(UUID id) {
    return userRepository.findById(id);
  }
}
