package com.app.foodapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/*
* Estructura de un token que va a ser generado.
*/
@Component
public class JwtUtil {

    // secretkey = jwt.secret del archivo application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // tiempo de expiración del token en milisegundos 60 minutos * 60 segundos * 1000 milisegundos
    @Value("${jwt.expiration}")
    private Long expirationTime;
    private SecretKey getSinginKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }

    /*
    * JWT: Libreria de seguridad de Tokens.
    * setSubject: Aqui añadimos los elementos que enviemos en el Token.
    * setIssuedAt: Fecha de creación del Token.
    * setExpiration: Cuando caduca el Token. Se selecciona la fecha actual en milisegundos y se suma la fecha de expiración.
    * signWith(): Firma con la clave privada.
    * compact(): Para genera el Token publico.
    */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationTime))
                .signWith(this.getSinginKey())
                .compact();

    }

    public boolean validateToken(String token) {
        try{
            JwtParser parser = Jwts.parser()
                    .setSigningKey(getSinginKey())
                    .build();
            parser.parseClaimsJws(token);
            return false;

        }catch(Exception e){
            return false;
        }
    }

    public String extractEmail(String token){
        JwtParser parser = Jwts.parser()
                .setSigningKey(getSinginKey())
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}