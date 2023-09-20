package com.example.springbootwithjwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

     @Value("${security.jwt.secret-key}")
     private String secreteKey;
     @Value("${security.jwt.expire-length}")
     private long expirationTime;
    public boolean isTokenValid(String jwtToken){
        return isSignatureValid(jwtToken) && !isTokenExpired(jwtToken);
    }

    public String getUsername(String jwtToken){
        return extractAllClaims(jwtToken).get("username",String.class);
    }
    public String getAuthority(String jwtToken){
        return extractAllClaims(jwtToken).get("authority",String.class);
    }
    public String buildToken(String username, String role){
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("authority",role);
        map.put("expires_data",new Date(System.currentTimeMillis()+expirationTime));
        return buildToken(map);
    }
    private String buildToken(Map<String,?>claims){
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String jwtToken){
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

    }
    private boolean isTokenExpired(String jwtToken) {
        Date expiration = extractAllClaims(jwtToken).get("expires_data",Date.class);
        System.out.println(expiration);
        return expiration.before(new Date());
    }
    private boolean isSignatureValid(String jwtToken){
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
