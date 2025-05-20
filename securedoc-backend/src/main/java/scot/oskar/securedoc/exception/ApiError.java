package scot.oskar.securedoc.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

  private LocalDateTime timestamp;
  private HttpStatus status;
  private String message;

  @Builder.Default
  private List<String> errors = List.of();

  public ApiError() {
    this.timestamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApiError(HttpStatus status, String message) {
    this();
    this.status = status;
    this.message = message;
  }

  public ApiError(HttpStatus status, @NonNull Throwable ex) {
    this();
    this.status = status;
    this.message = ex.getMessage();
  }

  public void addError(String error) {
    this.errors.add(error);
  }

}
