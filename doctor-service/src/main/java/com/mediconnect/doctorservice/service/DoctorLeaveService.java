package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.requestDtos.LeaveRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.LeaveResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorLeaveService {

    ApiResponse<LeaveResponse> createLeave(UUID authenticatedUserId, LeaveRequest request);

    ApiResponse<List<LeaveResponse>> getLeavesByDoctor(UUID doctorId);

    ApiResponse<String> deleteLeave(UUID authenticatedUserId, Long leaveId);
}