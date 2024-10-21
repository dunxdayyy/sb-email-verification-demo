package com.example.demo.user;

import com.example.demo.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    Optional<User> findByEmail(String email);
}
