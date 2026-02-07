package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.requestDtos.AvailabilityRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.AvailabilityResponse;
import com.mediconnect.doctorservice.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<AvailabilityResponse> addAvailability(
            @RequestBody AvailabilityRequest request) {

        AvailabilityResponse response =
                availabilityService.addAvailability(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<List<AvailabilityResponse>>> getAvailability(
            @PathVariable UUID doctorId) {

        return ResponseEntity.ok(
                availabilityService.getAvailabilityByDoctor(doctorId)
        );
    }

    @PutMapping("/{availabilityId}")
    public ResponseEntity<AvailabilityResponse> updateAvailability(
            @PathVariable Long availabilityId,
            @RequestBody AvailabilityRequest request) {

        return ResponseEntity.ok(
                availabilityService.updateAvailability(availabilityId, request)
        );
    }

    @DeleteMapping("/{availabilityId}")
    public ResponseEntity<ApiResponse<String>> deleteAvailability(
            @PathVariable Long availabilityId) {

        availabilityService.deleteAvailability(availabilityId);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Availability deleted successfully")
                .data("Deleted")
                .build();
        return ResponseEntity.ok(response);
    }
}
