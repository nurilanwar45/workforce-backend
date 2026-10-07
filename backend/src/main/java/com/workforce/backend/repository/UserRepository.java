package com.workforce.backend.repository;

import com.workforce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // <-- Pastikan import ini ada

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Senjata baru untuk mencari teknisi berdasarkan email
    Optional<User> findByEmail(String email);
    
}