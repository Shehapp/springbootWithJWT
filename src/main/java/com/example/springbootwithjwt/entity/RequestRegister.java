package com.example.springbootwithjwt.entity;

import lombok.Data;

@Data
public class RequestRegister {
    private String username;
    private String email;
    private String password;
}
