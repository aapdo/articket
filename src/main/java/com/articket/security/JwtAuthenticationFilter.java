package com.articket.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationFilter {
    private String email;
    private String password;
}
