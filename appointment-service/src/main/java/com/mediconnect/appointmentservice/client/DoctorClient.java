package com.mediconnect.appointmentservice.client;


import com.mediconnect.appointmentservice.DTO.responseDTO.AvailabilityResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.DoctorResponse;

import com.mediconnect.appointmentservice.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/api/doctors/{doctorId}")
    ApiResponse<DoctorResponse> getDoctorById(@PathVariable UUID doctorId);

    @GetMapping("/api/doctors/availability/{doctorId}")
    ApiResponse<List<AvailabilityResponse>> getAvailability(@PathVariable UUID doctorId);
}