package com.langstory.auth.controller;

import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    @GetMapping("/hello-to-user")
    public String helloToUser(HttpServletRequest httpServletRequest) {

        log.info(httpServletRequest.getHeader("Custom-Header"));

        ServiceInstance userService = discoveryClient.getInstances("user-service").get(0);

        return restClient.get()
                .uri(userService.getUri() + "/api/v1/user/hello-user")
                .retrieve()
                .body(String.class);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        authService.register(request);
        return "User registered successfully";
    }
}
