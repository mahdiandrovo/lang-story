package com.langstory.auth.service;

import com.langstory.auth.clients.UserFeignClient;
import com.langstory.auth.dto.SignInRequest;
import com.langstory.auth.dto.SignUpRequest;
import com.langstory.auth.dto.UserDto;
import com.langstory.auth.dto.UserResponse;
import com.langstory.auth.entity.AuthUserEntity;
import com.langstory.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFeignClient userFeignClient;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //@Retry(name = "userRetry", fallbackMethod = "registerFallback")
    //@RateLimiter(name = "userRateLimiter", fallbackMethod = "registerFallback")
    //@CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "registerFallback")
    public String signUp(SignUpRequest request) {

        log.info("Calling the register method");

        // check if user already exists
        if (authUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // save user in authDB
        AuthUserEntity newUser = new AuthUserEntity();
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
            userFeignClient.createUser(userDto);

            //generate and return token
            return jwtService.generateToken(newUser);
        } catch (Exception e) {
            // rollback auth-user creation
            authUserRepository.delete(newUser);
            log.error("Registration failed: {}", e.getMessage());
            throw new RuntimeException("Registration failed: user-service unavailable");
        }
    }

    public String signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AuthUserEntity authUserEntity = (AuthUserEntity) authentication.getPrincipal();

        return jwtService.generateToken(authUserEntity);
    }

    public UserResponse registerFallback(SignUpRequest request, Throwable throwable) {
        log.error("Fallback occured due to: {}", throwable.getMessage());
        return  new UserResponse();
    }
}
