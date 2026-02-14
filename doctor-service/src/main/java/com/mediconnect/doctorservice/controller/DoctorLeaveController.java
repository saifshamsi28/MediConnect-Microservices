package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.requestDtos.LeaveRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.LeaveResponse;
import com.mediconnect.doctorservice.service.DoctorLeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors/leaves")
@RequiredArgsConstructor
@Slf4j
public class DoctorLeaveController {

    private final DoctorLeaveService leaveService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<LeaveResponse>> createLeave(
            Authentication authentication,
            @RequestBody LeaveRequest request) {

        UUID userId = UUID.fromString(authentication.getName());
        ApiResponse<LeaveResponse> response = leaveService.createLeave(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<List<LeaveResponse>>> getLeavesByDoctor(
            @PathVariable UUID doctorId) {

        ApiResponse<List<LeaveResponse>> response = leaveService.getLeavesByDoctor(doctorId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{leaveId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<String>> deleteLeave(
            Authentication authentication,
            @PathVariable Long leaveId) {

        UUID userId = UUID.fromString(authentication.getName());
        ApiResponse<String> response = leaveService.deleteLeave(userId, leaveId);

        return ResponseEntity.ok(response);
    }
}