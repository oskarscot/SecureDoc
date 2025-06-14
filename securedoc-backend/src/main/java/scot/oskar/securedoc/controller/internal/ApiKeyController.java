package scot.oskar.securedoc.controller.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scot.oskar.securedoc.annotation.CurrentUser;
import scot.oskar.securedoc.data.ApiResponse;
import scot.oskar.securedoc.data.model.ApiKey;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.service.impl.ApiKeyService;

@RestController
@RequestMapping("api/v1/developer/key")
public class ApiKeyController {

  private final ApiKeyService apiKeyService;

  public ApiKeyController(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  @PostMapping
  public ResponseEntity<?> createApiKey(@CurrentUser User user) {
    final ApiKey apiKey = apiKeyService.createApiKey(user);

    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("Api key generated")
        .resource(apiKey)
        .build();

    return ResponseEntity.ok(response);
  }
}
