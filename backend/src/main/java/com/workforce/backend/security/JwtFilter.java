package com.workforce.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Ini adalah metode pemeriksaan yang akan jalan setiap kali ada request masuk
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Periksa bagian saku/header yang bernama "Authorization"
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // 2. Jika ada tiket dan diawali dengan stempel "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Potong tulisan "Bearer " (7 huruf) untuk mengambil tiket aslinya
            try {
                email = jwtUtil.extractEmail(jwt); // Ambil nama/email dari tiket
            } catch (Exception e) {
                System.out.println("Tiket JWT rusak atau kadaluarsa!");
            }
        }

        // 3. Jika email valid dan user belum terdaftar di catatan masuk saat ini
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cek ke buku daftar karyawan (database)
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);

            // Jika tiket asli dan belum kadaluarsa
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                // Berikan akses penuh (Buka pintu gerbang)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 4. Lanjutkan perjalanan ke Controller (entah pintunya tadi dibuka atau tetap ditutup)
        filterChain.doFilter(request, response);
    }
}