package com.langstory.auth.controller;

import com.langstory.auth.clients.UserFeignClient;
import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.dto.UserResponse;
import com.langstory.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final UserFeignClient userFeignClient;

    @GetMapping("/hello-to-user")
    public String helloToUser(HttpServletRequest httpServletRequest, @RequestHeader("X-User-Id") Long userId) {

        log.info(httpServletRequest.getHeader("Custom-Header"));
        log.info("User ID is: {}", userId);

        return userFeignClient.helloToUserService();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
}
