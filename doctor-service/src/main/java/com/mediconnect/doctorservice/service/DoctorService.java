package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorResponse createDoctor(DoctorRequest request);

    List<DoctorResponse> createDoctorsBulk(List<DoctorRequest> requests);

    ApiResponse<List<DoctorResponse>> getDoctorsByFilters(
            Boolean active,
            int page,
            int size,
            String sortBy,
            String order
    );

    DoctorResponse getDoctorById(UUID doctorId);

}
