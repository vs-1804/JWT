package com.jwt;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTutil {
	static String secret="4a87c0f2b3e95d174f9618b9e0a5cf5e20a9b1c62ab789cfc370e349b3a8ed56";
	 static byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

    private static SecretKey SECRET_KEY=Keys.hmacShaKeyFor(keyBytes);
   
    
    // Generate JWT token for user
    public static  String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Role", userDetails.getRole());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Create token
    private static String doGenerateToken(Map<String, Object> claims, String username) {
    	  final Date createdDate = new Date();
    	  final Date expirationDate = new Date(createdDate.getTime() + 6 * 1000);
    	  return Jwts.builder()
    	      .claims(claims)
    	      .subject(username)
    	      .issuedAt(createdDate)
    	      .expiration(expirationDate)
    	      .signWith(SECRET_KEY)
    	      .compact();
    	}

    // Extract username from token
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claim from token using provided claims resolver function
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        Object role = claims.get("Role");
        System.out.println((String)role);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private static Claims extractAllClaims(String token) {
       
     return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    // Validate if token is expired
    private static Boolean isTokenExpired(String token) {
        return false; //extractExpiration(token).before(new Date());
    }

    // Validate token
   public static  Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
