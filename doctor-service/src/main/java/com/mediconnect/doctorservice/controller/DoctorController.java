package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.ApiResponse;
import com.mediconnect.doctorservice.dto.DoctorRequest;
import com.mediconnect.doctorservice.dto.DoctorResponse;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.createDoctor(request);

        return ResponseEntity
                .created(URI.create("/api/doctors/" + response.getDoctorId()))
                .body(response);
    }

    @PostMapping("/bulk")
    public List<DoctorResponse> createDoctorsBulk(@RequestBody List<DoctorRequest> requests) {
        return doctorService.createDoctorsBulk(requests);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsByFilters(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(
                doctorService.getDoctorsByFilters(active, page, size, sortBy, order)
        );
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(doctorService.getDoctorById(doctorId));
    }

}

