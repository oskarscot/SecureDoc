package scot.oskar.securedoc.controller.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scot.oskar.securedoc.annotation.CurrentUser;
import scot.oskar.securedoc.data.ApiResponse;
import scot.oskar.securedoc.data.dto.user.UserResponseDTO;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.service.impl.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(@CurrentUser User user) {
    return ResponseEntity.ok(ApiResponse.builder()
        .success(true)
        .message("User details retrieved")
        .resource(UserResponseDTO.fromUser(user))
        .build());
  }
}
