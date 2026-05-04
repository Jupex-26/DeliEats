package org.example.ordersservice.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey secretKey; // Clave secreta para firmar el token

    public String generateToken(User user) {
        Instant now = Instant.now();
        Date expiryDate = Date.from(now.plus(30, ChronoUnit.DAYS));

        return Jwts.builder()
                // Identificador principal del usuario (usamos el email)
                .setSubject(user.getEmail())
                // La clave secreta para firmar el token y saber que es nuestro cuando lleguen las peticiones del frontend
                .signWith(secretKey, SignatureAlgorithm.HS256)
                // Fecha emisión del token
                .setIssuedAt(Date.from(now))
                // Fecha de expiración del token
                .setExpiration(expiryDate)
                // información personalizada: rol o roles, username, avatar...
                // .claim("role", user.getRole())
                //.claim("avatar", user.getAvatarUrl())
                // Construye el token
                .compact();


    }

    /*
    * Comprueba si un token es válido
    * */
    public boolean isTokenValid(String token, User usuario) {
        String email = extractEmail(token);
        return email.equals(usuario.getEmail()) && !isTokenExpired(token);
    }

    /*
    * Comprueba si un token ha expirado
    * */
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    /*
    * leer el token que envía el cliente y averiguar a qué usuario pertenece
    * */
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
