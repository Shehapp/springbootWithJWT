package com.example.springbootwithjwt.service;

import com.example.springbootwithjwt.config.JwtService;
import com.example.springbootwithjwt.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    final public UserService userService;
    final public PasswordEncoder passwordEncoder;
    final public JwtService jwtService;
    final public AuthenticationManager authenticationManager;

    public ResponseToken register(RequestRegister register){

        User user=new User();
        user.setUsername(register.getUsername());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setRole(Role.USER);
        userService.save(user);

        // create jwt token
        return ResponseToken
                .builder()
                .token(jwtService
                        .buildToken(user.getUsername(),
                                    user.getRole().toString()))
                .build();
    }

    public ResponseToken authenticate(RequestAuthenticate authenticate){

        // create Authentication object and pass it to AuthenticationManager
        // to authenticate the user
        Authentication authentication;
        UsernamePasswordAuthenticationToken authenticationToken
                =new UsernamePasswordAuthenticationToken(
                        authenticate.getUsername(),
                        authenticate.getPassword());
        try {
             authentication = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            throw new RuntimeException("Invalid username/password supplied");
        }

        String role=authentication.getAuthorities().stream().findFirst().get().getAuthority();
        role.replace("[","");
        role.replace("]","");
        System.out.println(role);
        return ResponseToken
                .builder()
                .token(jwtService
                        .buildToken(authenticate.getUsername(),
                                role))
                .build();
    }

}
