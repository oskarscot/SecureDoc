package scot.oskar.securedoc.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import scot.oskar.securedoc.annotation.resolver.CurrentUserArgumentResolver;
import scot.oskar.securedoc.service.impl.UserService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final UserService userService;

  public WebConfig(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new CurrentUserArgumentResolver(userService));
  }
}
