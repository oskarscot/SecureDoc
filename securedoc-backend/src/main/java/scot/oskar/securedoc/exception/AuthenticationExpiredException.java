package scot.oskar.securedoc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthenticationExpiredException extends RuntimeException {

  public AuthenticationExpiredException(String message) {
    super(message);
  }
}
