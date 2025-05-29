package scot.oskar.securedoc.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

  private LocalDateTime timestamp;
  private HttpStatus status;
  private String message;

  private final List<String> errors = new ArrayList<>();

  public void addError(String error) {
    this.errors.add(error);
  }

  public ApiError(HttpStatus status, String message) {
    this.timestamp = LocalDateTime.now();
    this.message = message;
    this.status = status;
  }

}
