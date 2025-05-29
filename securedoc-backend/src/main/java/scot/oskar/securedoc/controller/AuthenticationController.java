package scot.oskar.securedoc.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import scot.oskar.securedoc.data.ApiResponse;
import scot.oskar.securedoc.data.dto.UserSignupDTO;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.service.impl.AuthenticationService;

@RestController()
@RequestMapping("api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService service;

  public AuthenticationController(AuthenticationService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserSignupDTO dto) {
    final User user = service.createUser(dto);

    final URI createdUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/users/{username}")
        .buildAndExpand(user.getUsername()).toUri();

    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("User created sucessfully")
        .resource(user)
        .build();

    return ResponseEntity.created(createdUri).body(response);
  }

}
