package scot.oskar.securedoc.security;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.ResourceNotFoundException;
import scot.oskar.securedoc.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(final UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() ->
            new ResourceNotFoundException("User", "id", id));

    return UserPrincipal.create(user);
  }
}
