package scot.oskar.securedoc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> resourceNotFoundHandler(ResourceNotFoundException ex) {
    final ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
    return this.buildResponse(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> validationHandler(MethodArgumentNotValidException ex) {
    final ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Invalid request body.");

    ex.getBindingResult().getFieldErrors()
        .stream()
        // Turns the error into a string of "field: message"
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .forEach(error::addError);

    return this.buildResponse(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<Object> authenticationHandler(AuthenticationException ex) {
    final ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return this.buildResponse(error);
  }

  @ExceptionHandler(AuthenticationExpiredException.class)
  protected ResponseEntity<Object> authenticationExpiredHandler(AuthenticationExpiredException ex) {
    final ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return this.buildResponse(error);
  }

  protected ResponseEntity<Object> buildResponse(@NonNull ApiError apiError) {
    return ResponseEntity
        .status(apiError.getStatus())
        .body(apiError);
  }
}
