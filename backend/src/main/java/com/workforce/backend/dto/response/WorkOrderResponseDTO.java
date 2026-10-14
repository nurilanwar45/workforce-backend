package com.workforce.backend.dto.response;

import com.workforce.backend.entity.WorkOrder;

public class WorkOrderResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String technicianName;
    private String technicianSpecialty;

    // Constructor kosong wajib untuk Spring
    public WorkOrderResponseDTO() {}

    // Constructor untuk mapping otomatis
    public WorkOrderResponseDTO(WorkOrder wo) {
        this.id = wo.getId();
        this.title = wo.getTitle();
        this.description = wo.getDescription();
        this.status = wo.getStatus();
        if (wo.getAssignedUser() != null) {
            this.technicianName = wo.getAssignedUser().getName();
            this.technicianSpecialty = wo.getAssignedUser().getSpecialty();
        }
    }

    // --- GETTER & SETTER MANUAL (Pengganti @Data Lombok) ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public String getTechnicianSpecialty() { return technicianSpecialty; }
    public void setTechnicianSpecialty(String technicianSpecialty) { this.technicianSpecialty = technicianSpecialty; }
}