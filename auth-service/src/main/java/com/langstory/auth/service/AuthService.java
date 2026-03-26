package com.langstory.auth.service;

import com.langstory.auth.client.UserServiceClient;
import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.dto.UserDto;
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
    private final UserServiceClient userServiceClient;

    public void register(RegisterRequest request) {

        // check if user already exists
        if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // save user in authDB
        AuthUser newUser = new AuthUser();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        authUserRepository.save(newUser);

        // prepare dto for user-service
        UserDto userDto = UserDto.builder()
                .authId(newUser.getId().toString())
                .name(request.getName())
                .email(request.getEmail())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .build();

        try{
            // call user-service via OpenFeign
            userServiceClient.createUser(userDto);
        } catch (Exception e) {
            // rollback auth-user creation
            authUserRepository.delete(newUser);
            throw new RuntimeException("Registration failed: User-service unavailable");
        }
    }
}
