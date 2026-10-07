package com.workforce.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.workforce.backend.entity.WorkOrder;
import com.workforce.backend.exception.ResourceNotFoundException;
import com.workforce.backend.dto.WorkOrderResponseDTO;
import com.workforce.backend.entity.User;
import com.workforce.backend.repository.WorkOrderRepository;
import com.workforce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderControlle {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private UserRepository userRepository;

   // 1. Ambil data tiket kAerja dengan Pagination & DTO
    @GetMapping
    public Page<WorkOrderResponseDTO> getAllWorkOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Membuat aturan halaman
        Pageable aturanHalaman = PageRequest.of(page, size);
        
        // Mengambil data dari database work_orders (BUKAN userRepository)
        Page<WorkOrder> dataMentah = workOrderRepository.findAll(aturanHalaman);
        
        // Mengubah ke format DTO tiket kerja
        return dataMentah.map(wo -> new WorkOrderResponseDTO(wo));
    }
    @GetMapping("/{id}")
    public WorkOrder getWorkOrderById(@PathVariable Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order tidak ditemukan"));
            
    }
    @GetMapping("/user/{userId}")
    public List<WorkOrder> getWorkOrdersByUser(@PathVariable Long userId) {
        return workOrderRepository.findByAssignedUserId(userId);
    }

    @PostMapping
    public WorkOrder createWorkOrder(@RequestBody WorkOrder workOrder) {
        if (workOrder.getAssignedUser() != null && workOrder.getAssignedUser().getId() != null) {
            User user = userRepository.findById(workOrder.getAssignedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teknisi tidak ditemukan"));
            workOrder.setAssignedUser(user);
        }
        return workOrderRepository.save(workOrder);
    }

    @PutMapping("/{id}")
    public WorkOrder updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrder detailBaru) {
        WorkOrder woLama = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order tidak ditemukan"));

        woLama.setTitle(detailBaru.getTitle());
        woLama.setDescription(detailBaru.getDescription());
        woLama.setLocation(detailBaru.getLocation());
        woLama.setStatus(detailBaru.getStatus());

        if (detailBaru.getAssignedUser() != null && detailBaru.getAssignedUser().getId() != null) {
            User user = userRepository.findById(detailBaru.getAssignedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teknisi tidak ditemukan"));
            woLama.setAssignedUser(user);
        } else if (detailBaru.getAssignedUser() == null) {
            woLama.setAssignedUser(null);
        }

        return workOrderRepository.save(woLama);
    }

    @DeleteMapping("/{id}")
    public String deleteWorkOrder(@PathVariable Long id) {
        workOrderRepository.deleteById(id);
        return "Work Order dengan ID " + id + " berhasil dihapus!";
    }
}