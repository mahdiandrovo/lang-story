package com.langstory.auth.service;

import com.langstory.auth.clients.UserFeignClient;
import com.langstory.auth.dto.*;
import com.langstory.auth.entity.AuthUserEntity;
import com.langstory.auth.entity.RefreshTokenEntity;
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
    private final RefreshTokenService refreshTokenService;

    //@Retry(name = "userRetry", fallbackMethod = "registerFallback")
    //@RateLimiter(name = "userRateLimiter", fallbackMethod = "registerFallback")
    //@CircuitBreaker(name = "userCircuitBreaker", fallbackMethod = "registerFallback")
    public AuthResponse signUp(SignUpRequest request) {

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
            return generateAuthResponse(newUser);
        } catch (Exception e) {
            // rollback auth-user creation
            authUserRepository.delete(newUser);
            log.error("Registration failed: {}", e.getMessage());
            throw new RuntimeException("Registration failed: user-service unavailable");
        }
    }

    public AuthResponse signIn(SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AuthUserEntity authUserEntity = (AuthUserEntity) authentication.getPrincipal();

        return generateAuthResponse(authUserEntity);
    }

    public AuthResponse refresh(String refreshToken) {

        // validate refresh token
        RefreshTokenEntity validToken = refreshTokenService.validateRefreshToken(refreshToken);

        // get user by userId from refresh token
        AuthUserEntity authUser = authUserRepository.findById(validToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // generate new access token only
        String newAccessToken = jwtService.generateAccessToken(authUser);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }

    private AuthResponse generateAuthResponse(AuthUserEntity authUserEntity) {
        String accessToken = jwtService.generateAccessToken(authUserEntity);
        String refreshToken = refreshTokenService.createRefreshToken(authUserEntity.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserResponse registerFallback(SignUpRequest request, Throwable throwable) {
        log.error("Fallback occured due to: {}", throwable.getMessage());
        return  new UserResponse();
    }
}
