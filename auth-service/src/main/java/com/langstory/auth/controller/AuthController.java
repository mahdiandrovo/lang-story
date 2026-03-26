package com.langstory.auth.controller;

import com.langstory.auth.dto.RegisterRequest;
import com.langstory.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    @GetMapping("/hello-to-user")
    public String helloToUser(){
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
