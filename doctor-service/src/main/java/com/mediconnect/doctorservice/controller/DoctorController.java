package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody DoctorRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> createDoctorsBulk(@RequestBody List<DoctorRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctorsBulk(requests));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsByFilters(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByFilters(active, name, page, size, sortBy, order)
        );
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorById(doctorId));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<String>> deleteDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(doctorService.deleteDoctorById(doctorId));
    }
}

