package com.example.springbootwithjwt.service;

import com.example.springbootwithjwt.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<User> getUser(String username);
    void save(User user);
}
