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
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

        private final AppointmentService appointmentService;

        @PostMapping("/book")
        public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
                        @RequestBody AppointmentRequest request) {
                AppointmentResponse appointment = appointmentService.bookAppointment(request);

                ApiResponse<AppointmentResponse> response = ApiResponse.<AppointmentResponse>builder()
                                .success(true)
                                .message("Appointment booked successfully")
                                .data(appointment)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/available-slots/{doctorId}")
        public ResponseEntity<ApiResponse<List<SlotResponse>>> getAvailableSlots(
                        @PathVariable("doctorId") UUID doctorId,
                        @RequestParam("date") String date) {
                List<SlotResponse> slots = appointmentService.getAvailableSlots(doctorId, date);
                ApiResponse<List<SlotResponse>> response = ApiResponse.<List<SlotResponse>>builder()
                                .success(true)
                                .message("Available slots fetched successfully")
                                .data(slots)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/patient/{patientId}")
        public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments(
                        @PathVariable("patientId") UUID patientId) {
                List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatient(patientId);
                ApiResponse<List<AppointmentResponse>> response = ApiResponse.<List<AppointmentResponse>>builder()
                                .success(true)
                                .message("Patient appointments fetched successfully")
                                .data(appointments)
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/doctor/{doctorId}")
        public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
                        @PathVariable("doctorId") UUID doctorId) {
                List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
                ApiResponse<List<AppointmentResponse>> response = ApiResponse.<List<AppointmentResponse>>builder()
                                .success(true)
                                .message("Doctor appointments fetched successfully")
                                .data(appointments)
                                .build();

                return ResponseEntity.ok(response);
        }

}