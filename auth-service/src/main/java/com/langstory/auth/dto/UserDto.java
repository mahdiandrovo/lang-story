package com.langstory.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserDto {
    private String authId;
    private String name;
    private LocalDate birthday;
    private String gender;
    private String email;

}
