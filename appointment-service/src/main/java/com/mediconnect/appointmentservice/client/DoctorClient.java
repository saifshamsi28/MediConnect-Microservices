package com.mediconnect.appointmentservice.client;


import com.mediconnect.appointmentservice.DTO.responseDTO.AvailabilityResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.DoctorResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.LeaveResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.ScheduleResponse;

import com.mediconnect.appointmentservice.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/api/v1/doctors/{doctorId}")
    ApiResponse<DoctorResponse> getDoctorById(@PathVariable("doctorId") UUID doctorId);

    @GetMapping("/api/v1/doctors/availability/{doctorId}")
    ApiResponse<List<AvailabilityResponse>> getAvailability(@PathVariable("doctorId") UUID doctorId);

    @GetMapping("/api/v1/doctors/leaves/{doctorId}")
    ApiResponse<List<LeaveResponse>> getLeaves(@PathVariable("doctorId") UUID doctorId);

    @GetMapping("/api/v1/doctors/schedules/{doctorId}")
    ApiResponse<List<ScheduleResponse>> getSchedules(@PathVariable("doctorId") UUID doctorId);
}