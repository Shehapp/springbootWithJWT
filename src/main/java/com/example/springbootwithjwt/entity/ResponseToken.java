package com.example.springbootwithjwt.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseToken {
    String token;
}
