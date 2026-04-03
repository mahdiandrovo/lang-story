package com.langstory.auth.controller;

import com.langstory.auth.dto.AuthResponse;
import com.langstory.auth.dto.SignInRequest;
import com.langstory.auth.dto.SignUpRequest;
import com.langstory.auth.entity.AuthUserEntity;
import com.langstory.auth.entity.RefreshTokenEntity;
import com.langstory.auth.service.AuthService;
import com.langstory.auth.service.CookieService;
import com.langstory.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(
            @RequestBody SignUpRequest request,
            HttpServletResponse response
    ){
        AuthResponse authResponse = authService.signUp(request);
        cookieService.addRefreshTokenCookie(response, authResponse.getRefreshToken());
        authResponse.setRefreshToken(null);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(
            @RequestBody SignInRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.signIn(request);
        cookieService.addRefreshTokenCookie(response, authResponse.getRefreshToken());
        authResponse.setRefreshToken(null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken);
        cookieService.clearRefreshTokenCookie(response);
        return ResponseEntity.noContent().build();
    }
}
