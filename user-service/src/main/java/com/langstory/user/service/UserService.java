package com.langstory.user.service;

import com.langstory.user.entity.UserEntity;
import com.langstory.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity getUserByAuthId(String authId) {
        return userRepository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
