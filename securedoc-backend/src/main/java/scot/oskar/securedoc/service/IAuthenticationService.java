package scot.oskar.securedoc.service;

import scot.oskar.securedoc.data.dto.token.RefreshTokenDTO;
import scot.oskar.securedoc.data.dto.token.TokenResponseDTO;
import scot.oskar.securedoc.data.dto.user.UserLoginDTO;
import scot.oskar.securedoc.data.dto.user.UserSignupDTO;
import scot.oskar.securedoc.data.model.User;

public interface IAuthenticationService {

  User createUser(UserSignupDTO userSignup);

  TokenResponseDTO authenticateUser(UserLoginDTO userLogin);

  TokenResponseDTO refreshToken(RefreshTokenDTO dto);
}
