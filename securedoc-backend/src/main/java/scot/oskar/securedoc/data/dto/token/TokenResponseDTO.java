package scot.oskar.securedoc.data.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TokenResponseDTO {

  private String accessToken;
  private String refreshToken;

}
