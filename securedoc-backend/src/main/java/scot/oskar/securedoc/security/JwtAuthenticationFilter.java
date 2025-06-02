package scot.oskar.securedoc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import scot.oskar.securedoc.data.model.RefreshToken;
import scot.oskar.securedoc.service.impl.JwtTokenService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthenticationFilter(JwtTokenService tokenService, UserDetailsServiceImpl userDetailsService) {
    this.tokenProvider = tokenService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
          throws ServletException, IOException {
    String jwt = getTokenFromRequest(request);

    if (StringUtils.hasText(jwt)) {
      boolean isValid = tokenProvider.validateToken(jwt);

      if (isValid) {
        UUID userId = tokenProvider.getUserIdFromToken(jwt);

        RefreshToken refreshToken = tokenProvider.findByUserId(userId).orElse(null);
        if (refreshToken != null && refreshToken.getToken().equals(jwt)) {
          filterChain.doFilter(request, response);
          return;
        }

        UserDetails userDetails = userDetailsService.loadUserById(userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
