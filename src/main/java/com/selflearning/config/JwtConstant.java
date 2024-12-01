package com.selflearning.config;

import java.util.Base64;

public class JwtConstant {
    // Generate a key using Keys.secretKeyFor(SignatureAlgorithm.HS256)
    public static final String SECRET_KEY = Base64.getEncoder().encodeToString("starkindustries2024supersecurekey".getBytes());
    public static final String JWT_HEADER = "Authorization";
}
