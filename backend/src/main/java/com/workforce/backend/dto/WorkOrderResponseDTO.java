package com.workforce.backend.dto;

import com.workforce.backend.entity.WorkOrder;
import java.time.LocalDateTime;

public class WorkOrderResponseDTO {
    private Long id;
    private String title;
    private String location;
    private String status;
    private LocalDateTime createdAt;
    
    // Kita ganti objek User yang panjang menjadi String nama saja!
    private String assignedTechnicianName; 

    public WorkOrderResponseDTO(WorkOrder wo) {
        this.id = wo.getId();
        this.title = wo.getTitle();
        this.location = wo.getLocation();
        this.status = wo.getStatus();
        this.createdAt = wo.getCreatedAt();
        
        // Logika penyaringan: Kalau ada teknisinya, ambil namanya. Kalau kosong, tulis "Belum ditugaskan"
        if (wo.getAssignedUser() != null) {
            this.assignedTechnicianName = wo.getAssignedUser().getName();
        } else {
            this.assignedTechnicianName = "Belum ditugaskan";
        }
    }

    // --- GETTER ---
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getAssignedTechnicianName() { return assignedTechnicianName; }
}