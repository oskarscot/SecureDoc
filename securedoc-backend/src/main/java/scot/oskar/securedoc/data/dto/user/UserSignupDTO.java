package scot.oskar.securedoc.data.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDTO {

  @NotBlank(message = "Username cannot be empty")
  @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
  private String username;

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Provide a valid email")
  private String email;

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
  private String password;

}
