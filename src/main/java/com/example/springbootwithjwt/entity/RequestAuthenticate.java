package com.example.springbootwithjwt.entity;

import lombok.Data;

@Data
public class RequestAuthenticate {
    private String username;
    private String password;
}
