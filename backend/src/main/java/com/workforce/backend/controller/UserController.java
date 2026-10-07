package com.workforce.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workforce.backend.dto.UserResponseDTO;
import com.workforce.backend.entity.User;
import com.workforce.backend.exception.ResourceNotFoundException;
import com.workforce.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. Ambil SEMUA data user (GET /api/users)
   @GetMapping
public List<UserResponseDTO> getAllUsers() { // <-- Pastikan ini UserResponseDTO
    return userRepository.findAll().stream()
            .map(user -> new UserResponseDTO(user))
            .collect(Collectors.toList());
}

    // 2. Ambil 1 user berdasarkan ID (GET /api/users/{id})
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan"));
        
        // Bungkus data aslinya pakai topeng DTO sebelum dikirim
        return new UserResponseDTO(user);
    }

    // 3. Tambah user baru (POST /api/users)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 4. Update/Ubah data user (PUT /api/users/{id})
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User detailBaru) {
        User userLama = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan"));

        // Update semua informasi user
        userLama.setName(detailBaru.getName());
        userLama.setEmail(detailBaru.getEmail());
        userLama.setPhoneNumber(detailBaru.getPhoneNumber());
        userLama.setRole(detailBaru.getRole());
        userLama.setStatus(detailBaru.getStatus());
        userLama.setSpecialty(detailBaru.getSpecialty());

        return userRepository.save(userLama);
    }

    // 5. Hapus user (DELETE /api/users/{id})
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User dengan ID " + id + " berhasil dihapus!";
    }
}