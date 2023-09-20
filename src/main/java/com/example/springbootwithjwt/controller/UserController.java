package com.example.springbootwithjwt.controller;

import com.example.springbootwithjwt.entity.User;
import com.example.springbootwithjwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;


    @GetMapping
    List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    User getOneUser(@PathVariable("id") Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found!!"));
    }
}
