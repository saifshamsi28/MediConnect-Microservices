package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.requestDtos.ScheduleRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.ScheduleResponse;

import java.util.List;
import java.util.UUID;

public interface ScheduleService {

    ApiResponse<ScheduleResponse> addSchedule(ScheduleRequest request);

    ApiResponse<List<ScheduleResponse>> getScheduleByDoctor(UUID doctorId);

    ApiResponse<ScheduleResponse> updateSchedule(Long id, ScheduleRequest request);

    ApiResponse<String> deleteSchedule(Long id);
}

