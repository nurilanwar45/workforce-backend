package com.workforce.backend.dto.request;

public class WorkOrderRequestDTO {
    private String title;
    private String description;
    private Long assignedUserId;

    // Getter dan Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getAssignedUserId() { return assignedUserId; }
    public void setAssignedUserId(Long assignedUserId) { this.assignedUserId = assignedUserId; }
}