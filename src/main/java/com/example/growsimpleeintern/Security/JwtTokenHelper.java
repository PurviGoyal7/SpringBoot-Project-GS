package com.example.growsimpleeintern.Security;

import com.example.growsimpleeintern.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;
    private String secret = "jwtTokenKey";

    public String getUserIdFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println((userDetails.getId()));
        return doGenerateToken(claims, String.valueOf(userDetails.getId()));
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, User userDetails) {
        try {
            final String Id = getUserIdFromToken(token);
            return (Id.equals(String.valueOf(userDetails.getId())) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
