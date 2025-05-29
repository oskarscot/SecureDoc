package scot.oskar.securedoc.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scot.oskar.securedoc.data.dto.UserSignupDTO;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.UserAlreadyExistsException;
import scot.oskar.securedoc.repository.UserRepository;
import scot.oskar.securedoc.service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  @Transactional
  public User createUser(UserSignupDTO userSignup) {
    if(repository.existsByUsername(userSignup.getEmail()) ||
        repository.existsByEmail(userSignup.getEmail())) {
      throw new UserAlreadyExistsException("A user with these details already exists.");
    }

    User user = User.builder()
        .username(userSignup.getUsername())
        .email(userSignup.getEmail())
        .password(passwordEncoder.encode(userSignup.getPassword()))
        .build();

    repository.save(user);

    return user;
  }
}
