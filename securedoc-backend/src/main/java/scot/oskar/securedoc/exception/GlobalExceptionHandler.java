package scot.oskar.securedoc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> resourceNotFoundHandler(ResourceNotFoundException ex) {
    final ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex);
    return this.buildResponse(error);
  }

  protected ResponseEntity<Object> buildResponse(ApiError apiError) {
    return ResponseEntity
        .status(apiError.getStatus())
        .body(apiError);
  }
}
