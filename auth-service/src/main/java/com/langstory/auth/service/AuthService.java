package com.langstory.auth.service;

import com.langstory.auth.clients.UserFeignClient;
import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.dto.UserDto;
import com.langstory.auth.dto.UserResponse;
import com.langstory.auth.entity.AuthUser;
import com.langstory.auth.repository.AuthUserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFeignClient userFeignClient;

    @Retry(name = "userRetry", fallbackMethod = "registerFallback")
    //@RateLimiter(name = "userRateLimiter", fallbackMethod = "registerFallback")
    //@CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "registerFallback")
    public UserResponse register(RegisterRequest request) {

        log.info("Calling the register method");

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
            return userFeignClient.createUser(userDto);
        } catch (Exception e) {
            // rollback auth-user creation
            authUserRepository.delete(newUser);
            throw new RuntimeException("Registration failed: User-service unavailable");
        }
    }

    public UserResponse registerFallback(RegisterRequest request, Throwable throwable) {
        log.error("Fallback occured due to: {}", throwable.getMessage());
        return  new UserResponse();
    }
}
