package com.workforce.backend.dto.response;

public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    // Tambahkan Getter dan Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}