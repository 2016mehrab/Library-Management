package com.eshan.library.configs;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ssl.SslBundleProperties.Key;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "e5002cf67ea39b137235b1aec78dbf322111ed63cfbb0d5e396e430472833900";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public String extractRole(String jwtToken) {
        var role = extractClaim(jwtToken, claims -> claims.get("role", String.class));
        LoggerFactory.getLogger(getClass()).info("ROLE FROM TOKEN " + role);
        return role;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    private java.security.Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userdetails) {
        extraClaims.put("role",
                userdetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority)
                        .orElse(""));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userdetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateToken(
            UserDetails userdetails) {
        return generateToken(new HashMap<>(), userdetails);
    }

    public String generateTokenWithId(
            UserDetails userdetails, Integer userid) {
        return generateTokenWithId(new HashMap<>(), userdetails, userid);
    }

    public String generateTokenWithId(
            Map<String, Object> extraClaims,
            UserDetails userdetails, Integer userid) {
        extraClaims.put("role",
                userdetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority)
                        .orElse(""));
        extraClaims.put("userId", userid);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userdetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
