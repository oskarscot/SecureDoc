package scot.oskar.securedoc.service.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import scot.oskar.securedoc.data.model.ApiKey;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.ResourceAlreadyExistsException;
import scot.oskar.securedoc.repository.ApiKeyRepository;

@Service
public class ApiKeyService {

  private final ApiKeyRepository repository;

  public ApiKeyService(ApiKeyRepository repository) {
    this.repository = repository;
  }

  public ApiKey createApiKey(User user) {
    final boolean keyExists = repository.findByUser(user).isPresent();
    if(keyExists) {
      throw new ResourceAlreadyExistsException("ApiKey", "user", user.getId());
    }

    final ApiKey apiKey = ApiKey.builder()
        .key(UUID.randomUUID().toString())
        .owner(user)
        .build();

    repository.save(apiKey);

    return apiKey;
  }

}
