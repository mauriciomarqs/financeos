package com.financeos.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String gerarToken(UserDetails usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.getAuthorities()
                .iterator().next().getAuthority());
        return buildToken(claims, usuario);
    }

    private String buildToken(Map<String, Object> claims, UserDetails usuario) {
        long agora = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(agora))
                .expiration(new Date(agora + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isTokenValido(String token, UserDetails usuario) {
        try {
            String email = extrairEmail(token);
            return email.equals(usuario.getUsername()) && !isTokenExpirado(token);
        } catch (Exception e) {
            log.warn("Token invalido: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpirado(String token) {
        return extrairExpiracao(token).before(new Date());
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public Date extrairExpiracao(String token) {
        return extrairClaims(token).getExpiration();
    }

    public Long extrairExpiracaoMs(String token) {
        return extrairExpiracao(token).getTime();
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}