package scot.oskar.securedoc.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Provide a valid email")
  private String email;

  @NotBlank(message = "Password cannot be empty.")
  private String password;

}
