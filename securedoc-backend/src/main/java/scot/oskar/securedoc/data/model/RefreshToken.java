package scot.oskar.securedoc.data.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String token;
  private Instant expiry;

  // This has to be a ManyToOne relationship for edge cases where an existing token is not deleted before a new one is created.
  @ManyToOne
  @JoinColumn(name = "users_id")
  private User user;

}
