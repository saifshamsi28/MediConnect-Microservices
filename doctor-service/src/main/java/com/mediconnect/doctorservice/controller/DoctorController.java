package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.requestDtos.EducationRequest;
import com.mediconnect.doctorservice.dto.requestDtos.ExperienceRequest;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;
import com.mediconnect.doctorservice.dto.responseDtos.EducationResponse;
import com.mediconnect.doctorservice.dto.responseDtos.ExperienceResponse;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getMyProfile(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(doctorService.getDoctorByUserId(userId));
    }
    
    @PutMapping("/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID doctorId,
            @RequestBody DoctorRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(doctorService.updateDoctor(doctorId, request, userId));
    }
    
    @PostMapping("/{doctorId}/education")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<EducationResponse>> addEducation(
            @PathVariable UUID doctorId,
            @RequestBody EducationRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.addEducation(doctorId, request, userId));
    }
    
    @PostMapping("/{doctorId}/experience")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<ExperienceResponse>> addExperience(
            @PathVariable UUID doctorId,
            @RequestBody ExperienceRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.addExperience(doctorId, request, userId));
    }
    
    @DeleteMapping("/{doctorId}/education/{educationId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<String>> removeEducation(
            @PathVariable UUID doctorId,
            @PathVariable Long educationId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(doctorService.removeEducation(doctorId, educationId, userId));
    }
    
    @DeleteMapping("/{doctorId}/experience/{experienceId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<String>> removeExperience(
            @PathVariable UUID doctorId,
            @PathVariable Long experienceId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(doctorService.removeExperience(doctorId, experienceId, userId));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<String>> deleteDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(doctorService.deleteDoctorById(doctorId));
    }
}

