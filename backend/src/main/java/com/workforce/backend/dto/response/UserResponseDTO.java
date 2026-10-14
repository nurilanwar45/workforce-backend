package com.workforce.backend.dto.response;

import com.workforce.backend.entity.User;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String status;
    private String specialty;

    // Ini adalah 'Mesin Fotokopi' untuk menyaring data asli menjadi DTO
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.specialty = user.getSpecialty();
    }

    // --- GETTER --- (DTO biasanya hanya butuh Getter)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getSpecialty() { return specialty; }
}