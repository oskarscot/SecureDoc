package scot.oskar.securedoc.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scot.oskar.securedoc.data.dto.token.RefreshTokenDTO;
import scot.oskar.securedoc.data.dto.token.TokenResponseDTO;
import scot.oskar.securedoc.data.dto.user.UserLoginDTO;
import scot.oskar.securedoc.data.dto.user.UserSignupDTO;
import scot.oskar.securedoc.data.model.RefreshToken;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.ResourceNotFoundException;
import scot.oskar.securedoc.exception.UserAlreadyExistsException;
import scot.oskar.securedoc.repository.UserRepository;
import scot.oskar.securedoc.security.UserPrincipal;
import scot.oskar.securedoc.service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenService tokenService;

  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager, JwtTokenService tokenService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }


  @Override
  @Transactional
  public User createUser(UserSignupDTO userSignup) {
    if(userRepository.existsByUsername(userSignup.getEmail()) ||
        userRepository.existsByEmail(userSignup.getEmail())) {
      throw new UserAlreadyExistsException("A user with these details already exists.");
    }

    User user = User.builder()
        .username(userSignup.getUsername())
        .email(userSignup.getEmail())
        .password(passwordEncoder.encode(userSignup.getPassword()))
        .build();

    userRepository.save(user);

    return user;
  }

  @Override
  @Transactional
  public TokenResponseDTO authenticateUser(UserLoginDTO userLogin) {
    if(!userRepository.existsByEmail(userLogin.getEmail())) {
      throw new ResourceNotFoundException("User", "email", userLogin.getEmail());
    }

    // authenticate
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userLogin.getEmail(),
            userLogin.getPassword()
        ));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // retrieve principal and get user
    final UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    final User user = userRepository.findById(principal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));

    //generate access and refresh tokens
    final String jwtToken = tokenService.generateAccessToken(user.getId());
    final RefreshToken refreshToken = tokenService.generateRefreshToken(user.getId());

    final TokenResponseDTO response = TokenResponseDTO.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken.getToken()).build();
    return response;
  }

  @Override
  @Transactional
  public TokenResponseDTO refreshToken(RefreshTokenDTO dto) {
    return tokenService.findByToken(dto.getToken())
        .map(tokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          final String accessToken = tokenService.generateAccessToken(user.getId());
          return TokenResponseDTO.builder()
              .accessToken(accessToken)
              .refreshToken(dto.getToken())
              .build();
        }).orElseThrow(() -> new ResourceNotFoundException("Token", "id", dto.getToken()));
  }
}
