package scot.oskar.securedoc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse {

  private boolean success;
  private String message;
  private Object resource;

}
