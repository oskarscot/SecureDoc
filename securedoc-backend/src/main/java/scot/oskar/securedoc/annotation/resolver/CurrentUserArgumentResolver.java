package scot.oskar.securedoc.annotation.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import scot.oskar.securedoc.annotation.CurrentUser;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.ResourceNotFoundException;
import scot.oskar.securedoc.security.UserPrincipal;
import scot.oskar.securedoc.service.impl.UserService;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserService userService;

  public CurrentUserArgumentResolver(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class)
        && parameter.getParameterType().equals(User.class);
  }

  @Override
  public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    return userService.retrieveById(principal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));
  }
}
