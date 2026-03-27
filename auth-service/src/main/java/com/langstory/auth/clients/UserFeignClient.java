package com.langstory.auth.clients;

import com.langstory.auth.dto.UserDto;
import com.langstory.auth.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/v1/user")
public interface UserFeignClient {

    @GetMapping("/hello-user")
    String helloToUserService();

    @PostMapping("/create")
    UserResponse createUser(@RequestBody UserDto userDto);
}
