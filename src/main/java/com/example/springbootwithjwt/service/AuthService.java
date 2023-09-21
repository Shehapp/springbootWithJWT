package com.example.springbootwithjwt.service;

import com.example.springbootwithjwt.config.JwtService;
import com.example.springbootwithjwt.entity.*;
import com.example.springbootwithjwt.repository.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    final public UserService userService;
    final public PasswordEncoder passwordEncoder;
    final public JwtService jwtService;
    final public AuthenticationManager authenticationManager;
    final public RefreshTokenRepository tokenRepository;

    public ResponseTokens register(RequestRegister register){

        User user=new User();
        user.setUsername(register.getUsername());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setRole(Role.USER);
        userService.save(user);

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken=RefreshToken
                .builder()
                .token(token)
                .revoked(false)
                .expires(LocalDateTime.now().plusMinutes(2))
                .user(user)
                .build();
        tokenRepository.save(refreshToken);
        // create jwt token
        return ResponseTokens
                .builder()
                .accessToken(jwtService
                        .buildToken(user.getUsername(),
                                    user.getRole().toString()))
                .refreshToken(token)
                .build();
    }


    public void revoke(String username){
        List<RefreshToken> refreshTokens =
                tokenRepository.findByUserUsernameAndRevoked(
                        username,
                        false);
        if(refreshTokens!=null){
            refreshTokens.forEach(tt->
                    tt.setRevoked(true)

            );
            tokenRepository.saveAll(refreshTokens);
        }
    }

    public ResponseTokens authenticate(RequestAuthenticate authenticate){

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

        // revoke all tokens
        revoke(authenticate.getUsername());

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken=RefreshToken
                .builder()
                .token(token)
                .revoked(false)
                .expires(LocalDateTime.now().plusMonths(1))
                .user(userService.getUser(authenticate.getUsername()).get())
                .build();
        tokenRepository.save(refreshToken);

        String role=authentication.getAuthorities().stream().findFirst().get().getAuthority();
        role.replace("[","");
        role.replace("]","");
        System.out.println(role);
        return ResponseTokens
                .builder()
                .accessToken(jwtService
                        .buildToken(authenticate.getUsername(),
                                role))
                .refreshToken(token)
                .build();
    }

    public ResponseTokens authenticateByRefreshToken(String token) {

        RefreshToken refreshToken = tokenRepository.findByToken(token)
                .orElseThrow();
        if(refreshToken.isRevoked() || refreshToken.getExpires().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Invalid Token");

        User user=refreshToken.getUser();

        return ResponseTokens
                .builder()
                .accessToken(jwtService
                        .buildToken(user.getUsername(),
                                user.getRole().toString()))
                .refreshToken(token)
                .build();
    }
}
