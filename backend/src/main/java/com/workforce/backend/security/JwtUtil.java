package com.workforce.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Kunci Rahasia untuk stempel tiket (Harus panjang minimal 256-bit / 32 karakter!)
    private static final String SECRET_KEY = "KerjaBagusIniAdalahKunciRahasiaWorkforceSangatAman123!";
    
    // Tiket berlaku selama 24 Jam (dalam milidetik)
    private static final long TOKEN_VALIDITY = 1000 * 60 * 60 * 24;

    // Mesin pembuat stempel keamanan
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 1. BIKIN TIKET: Dipanggil saat user berhasil login
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. BACA TIKET: Mengambil email dari dalam tiket
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. VALIDASI TIKET: Mengecek apakah tiket cocok dengan email & belum kadaluarsa
    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    // --- ALAT BANTU (Internal) ---
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}