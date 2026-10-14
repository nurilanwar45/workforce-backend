package com.workforce.backend.dto.request;

public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String specialty;

    // Tambahkan Getter dan Setter (atau gunakan @Data jika memakai Lombok)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}