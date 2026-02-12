package com.mediconnect.doctorservice.service;


import com.mediconnect.doctorservice.dto.requestDtos.AvailabilityRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.AvailabilityResponse;

import java.util.List;
import java.util.UUID;

public interface AvailabilityService {

    ApiResponse<AvailabilityResponse> addAvailability(AvailabilityRequest request);

    ApiResponse<List<AvailabilityResponse>> getAvailabilityByDoctor(UUID doctorId);

    AvailabilityResponse updateAvailability(Long availabilityId, AvailabilityRequest request);

    void deleteAvailability(Long availabilityId);
}
