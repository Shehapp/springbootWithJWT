package com.example.springbootwithjwt.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expires;
    private boolean revoked;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;
}
