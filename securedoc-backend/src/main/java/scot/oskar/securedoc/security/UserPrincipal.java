package scot.oskar.securedoc.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;
import scot.oskar.securedoc.data.model.User;

@Getter
public class UserPrincipal implements UserDetails {

  private final UUID id;
  private final String username;
  private final String email;

  @JsonIgnore
  private final String password;

  public UserPrincipal(UUID id, String username, String email, String password) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public static UserPrincipal create(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }
}
