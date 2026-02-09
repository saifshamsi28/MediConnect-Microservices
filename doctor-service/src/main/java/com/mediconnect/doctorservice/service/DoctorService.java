package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    ApiResponse<DoctorResponse> createDoctor(DoctorRequest request);

    ApiResponse<List<DoctorResponse>> createDoctorsBulk(List<DoctorRequest> requests);

    ApiResponse<List<DoctorResponse>> getDoctorsByFilters(
            Boolean active,
            int page,
            int size,
            String sortBy,
            String order
    );

    ApiResponse<DoctorResponse> getDoctorById(UUID doctorId);

    ApiResponse<String> deleteDoctorById(UUID doctorId);

}
