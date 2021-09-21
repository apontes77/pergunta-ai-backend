package br.com.pucgo.perguntaai.config.security;

import br.com.pucgo.perguntaai.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        final User logged = (User) authentication.getPrincipal();
        final Date today = new Date();
        final Date dateExpiration = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API do fórum")
                .setSubject(logged.getId().toString())
                .setIssuedAt(today)
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        final Claims body = Jwts.parserBuilder()
                                .setSigningKey(this.secret)
                                .build()
                                .parseClaimsJws(token).getBody();
         return Long.parseLong(body.getSubject());
    }
}
