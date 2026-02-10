package com.mediconnect.appointmentservice.controller;


import com.mediconnect.appointmentservice.DTO.requestDTO.AppointmentRequest;
import com.mediconnect.appointmentservice.DTO.responseDTO.AppointmentResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.SlotResponse;
import com.mediconnect.appointmentservice.service.AppointmentService;
import com.mediconnect.appointmentservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @RequestBody AppointmentRequest request
    ) {
       AppointmentResponse appointment= appointmentService.bookAppointment(request);

        ApiResponse<AppointmentResponse> response = ApiResponse.<AppointmentResponse>builder()
                .success(true)
                .message("Appointment booked successfully")
                .data(appointment)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/available-slots/{doctorId}")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getAvailableSlots(
            @PathVariable UUID doctorId,
            @RequestParam String date
    ) {
        List<SlotResponse> slots=appointmentService.getAvailableSlots(doctorId, date);
        ApiResponse<List<SlotResponse>> response = ApiResponse.<List<SlotResponse>>builder()
                .success(true)
                .message("Available slots fetched successfully")
                .data(slots)
                .build();

        return ResponseEntity.ok(response);
    }

}