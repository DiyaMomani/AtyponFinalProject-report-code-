package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserToken {
    private String username;
    private String token;
    private String type;
}
