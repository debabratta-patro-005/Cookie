package com.springbootproject.cookie.controller;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class CookieController {

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    @GetMapping("/cookie")
    public String setCookie(HttpServletResponse response, @RequestParam String username,@RequestParam String role) {

        String token = generateToken(username, role);

        ResponseCookie cookie = ResponseCookie.from("Token", token).httpOnly(true).secure(false)
                .maxAge(3600).sameSite("none").build();

        response.addHeader("Set-Cookie", cookie.toString());


        return "Cookie added successfully!";
    }


    public String generateToken(String username, String role) {

        String token = Jwts.builder().setSubject(username)
                .claim("role", role)
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();

        return token;
    }
}
