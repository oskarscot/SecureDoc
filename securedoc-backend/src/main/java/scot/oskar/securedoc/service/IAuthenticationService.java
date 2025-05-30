package scot.oskar.securedoc.service;

import scot.oskar.securedoc.data.dto.UserLoginDTO;
import scot.oskar.securedoc.data.dto.UserSignupDTO;
import scot.oskar.securedoc.data.model.User;

public interface IAuthenticationService {

  User createUser(UserSignupDTO userSignup);

  User authenticateUser(UserLoginDTO userLogin);

}
