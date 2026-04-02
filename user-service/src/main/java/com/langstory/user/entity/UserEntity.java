package com.langstory.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authId;

    @Column(nullable = false)
    private String name;

    private LocalDate birthday;

    private String gender;

    @Column(unique = true)
    private String email;
}
