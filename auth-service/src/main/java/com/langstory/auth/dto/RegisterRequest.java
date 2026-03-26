package com.langstory.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;

    private String name;
    private LocalDate birthday;
    private String gender;
}
