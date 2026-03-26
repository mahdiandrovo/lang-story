package com.langstory.auth.client;

import com.langstory.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:9020/api/v1/user/create")
public interface UserServiceClient {
    @PostMapping
    String createUser(@RequestBody UserDto userDto);
}
