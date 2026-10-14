package com.workforce.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.workforce.backend.entity.WorkOrder;
import com.workforce.backend.exception.ResourceNotFoundException;
import com.workforce.backend.dto.response.WorkOrderResponseDTO; // Tambahkan .response
import com.workforce.backend.entity.User;
import com.workforce.backend.repository.WorkOrderRepository;
import com.workforce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController { // ✅ DIPERBAIKI: Tambah huruf 'r'

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Ambil data tiket kerja dengan Pagination & DTO (Sudah Benar)
    @GetMapping
    public Page<WorkOrderResponseDTO> getAllWorkOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable aturanHalaman = PageRequest.of(page, size);
        Page<WorkOrder> dataMentah = workOrderRepository.findAll(aturanHalaman);
        
        return dataMentah.map(wo -> new WorkOrderResponseDTO(wo));
    }

    // 2. Ambil detail dengan DTO agar aman
    @GetMapping("/{id}")
    public WorkOrderResponseDTO getWorkOrderById(@PathVariable Long id) {
        WorkOrder wo = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order tidak ditemukan"));
        
        return new WorkOrderResponseDTO(wo); // ✅ DIPERBAIKI: Gunakan DTO
    }

    // 3. Ambil data milik user dengan DTO agar aman
    @GetMapping("/user/{userId}")
    public List<WorkOrderResponseDTO> getWorkOrdersByUser(@PathVariable Long userId) {
        List<WorkOrder> workOrders = workOrderRepository.findByAssignedUserId(userId);
        
        // ✅ DIPERBAIKI: Konversi List<WorkOrder> menjadi List<WorkOrderResponseDTO>
        return workOrders.stream()
                .map(wo -> new WorkOrderResponseDTO(wo))
                .collect(Collectors.toList());
    }

    // 4. Create Work Order
    @PostMapping
    public WorkOrderResponseDTO createWorkOrder(@RequestBody WorkOrder workOrder) {
        if (workOrder.getAssignedUser() != null && workOrder.getAssignedUser().getId() != null) {
            User user = userRepository.findById(workOrder.getAssignedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teknisi tidak ditemukan"));
            workOrder.setAssignedUser(user);
        }
        WorkOrder savedWo = workOrderRepository.save(workOrder);
        return new WorkOrderResponseDTO(savedWo); // ✅ DIPERBAIKI: Kembalikan DTO
    }

    // 5. Update Work Order
    @PutMapping("/{id}")
    public WorkOrderResponseDTO updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrder detailBaru) {
        WorkOrder woLama = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order tidak ditemukan"));

        woLama.setTitle(detailBaru.getTitle());
        woLama.setDescription(detailBaru.getDescription());
        woLama.setLocation(detailBaru.getLocation()); // Asumsi field ini ada di entity-mu
        woLama.setStatus(detailBaru.getStatus());

        if (detailBaru.getAssignedUser() != null && detailBaru.getAssignedUser().getId() != null) {
            User user = userRepository.findById(detailBaru.getAssignedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teknisi tidak ditemukan"));
            woLama.setAssignedUser(user);
        } else if (detailBaru.getAssignedUser() == null) {
            woLama.setAssignedUser(null);
        }

        WorkOrder updatedWo = workOrderRepository.save(woLama);
        return new WorkOrderResponseDTO(updatedWo); // ✅ DIPERBAIKI: Kembalikan DTO
    }

    @DeleteMapping("/{id}")
    public String deleteWorkOrder(@PathVariable Long id) {
        workOrderRepository.deleteById(id);
        return "Work Order dengan ID " + id + " berhasil dihapus!";
    }
}