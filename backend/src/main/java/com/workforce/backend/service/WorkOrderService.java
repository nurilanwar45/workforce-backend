package com.workforce.backend.service;

import com.workforce.backend.dto.request.WorkOrderRequestDTO;
import com.workforce.backend.entity.User;
import com.workforce.backend.entity.WorkOrder;
import com.workforce.backend.exception.ResourceNotFoundException;
import com.workforce.backend.repository.UserRepository;
import com.workforce.backend.repository.WorkOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.workforce.backend.dto.response.WorkOrderResponseDTO;

import java.time.LocalDateTime;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, UserRepository userRepository) {
        this.workOrderRepository = workOrderRepository;
        this.userRepository = userRepository;
    }

    public WorkOrder createWorkOrder(WorkOrderRequestDTO request) {
        User assignedUser = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Teknisi dengan ID tersebut tidak ditemukan"));

        WorkOrder workOrder = new WorkOrder();
        workOrder.setTitle(request.getTitle());
        workOrder.setDescription(request.getDescription());
        workOrder.setStatus("PENDING");
        workOrder.setCreatedAt(LocalDateTime.now());
        workOrder.setAssignedUser(assignedUser);

        return workOrderRepository.save(workOrder);
    }

    // Mengambil data dengan Pagination dan mengubahnya menjadi DTO agar data sensitif aman
    public Page<WorkOrderResponseDTO> getAllWorkOrders(Pageable pageable) {
        Page<WorkOrder> workOrders = workOrderRepository.findAll(pageable);
        
        return workOrders.map(wo -> {
            WorkOrderResponseDTO dto = new WorkOrderResponseDTO();
            dto.setId(wo.getId());
            dto.setTitle(wo.getTitle());
            dto.setDescription(wo.getDescription());
            dto.setStatus(wo.getStatus());
            
            // Menyembunyikan password dan no HP teknisi di DTO
            if (wo.getAssignedUser() != null) {
                dto.setTechnicianName(wo.getAssignedUser().getName());
                dto.setTechnicianSpecialty(wo.getAssignedUser().getSpecialty());
            }
            return dto;
        });
    }
}