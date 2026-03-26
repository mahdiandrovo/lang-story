package com.langstory.auth.service;

import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.entity.AuthUser;
import com.langstory.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        // checking if user already exists or not
        if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // user does not exist, can create a new user
        AuthUser newUser = new AuthUser();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        authUserRepository.save(newUser);

        // later -> have to call user-service here

    }
}
