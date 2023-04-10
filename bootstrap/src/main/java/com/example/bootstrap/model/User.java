package com.example.bootstrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class User {
    private String username;
    private String token;
    private String workerId;
}
