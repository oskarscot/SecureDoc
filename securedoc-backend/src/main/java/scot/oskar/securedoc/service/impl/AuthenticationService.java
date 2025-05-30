package scot.oskar.securedoc.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scot.oskar.securedoc.data.dto.UserLoginDTO;
import scot.oskar.securedoc.data.dto.UserSignupDTO;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.ResourceNotFoundException;
import scot.oskar.securedoc.exception.UserAlreadyExistsException;
import scot.oskar.securedoc.repository.UserRepository;
import scot.oskar.securedoc.security.UserPrincipal;
import scot.oskar.securedoc.service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
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

  @Override
  @Transactional
  public User authenticateUser(UserLoginDTO userLogin) {
    if(!repository.existsByEmail(userLogin.getEmail())) {
      throw new ResourceNotFoundException("User", "email", userLogin.getEmail());
    }

    // authenticate
    // TODO Customise auth errors
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userLogin.getEmail(),
            userLogin.getPassword()
        ));
    // set the context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // retrieve principal and get user
    final UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    final User user = repository.findById(principal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));

    //TODO Generate access and refresh token

    return user;
  }
}
