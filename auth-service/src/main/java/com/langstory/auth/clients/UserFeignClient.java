package com.langstory.auth.clients;

import com.langstory.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/v1/user")
public interface UserFeignClient {

    @GetMapping("/hello-user")
    String helloToUserService();

    @PostMapping("/create")
    String createUser(@RequestBody UserDto userDto);
}
