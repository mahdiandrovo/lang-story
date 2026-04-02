package com.langstory.user.controller;

import com.langstory.user.entity.UserEntity;
import com.langstory.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserEntity> getUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByAuthId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
