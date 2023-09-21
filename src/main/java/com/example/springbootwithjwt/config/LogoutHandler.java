package com.example.springbootwithjwt.config;

import com.example.springbootwithjwt.entity.RefreshToken;
import com.example.springbootwithjwt.repository.RefreshTokenRepository;
import com.example.springbootwithjwt.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@AllArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {
    final public RefreshTokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        List<RefreshToken> refreshTokens =
                tokenRepository.findByUserUsernameAndRevoked(
                        authentication.getName(),
                        false);
        if(refreshTokens!=null){
            refreshTokens.forEach(tt->
                    tt.setRevoked(true)

            );
            tokenRepository.saveAll(refreshTokens);
        }

    }
}
