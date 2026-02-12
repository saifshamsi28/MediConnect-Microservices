package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.requestDtos.EducationRequest;
import com.mediconnect.doctorservice.dto.requestDtos.ExperienceRequest;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;
import com.mediconnect.doctorservice.dto.responseDtos.EducationResponse;
import com.mediconnect.doctorservice.dto.responseDtos.ExperienceResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    ApiResponse<DoctorResponse> createDoctor(DoctorRequest request);

    ApiResponse<List<DoctorResponse>> createDoctorsBulk(List<DoctorRequest> requests);

    ApiResponse<List<DoctorResponse>> getDoctorsByFilters(
            Boolean active,
            String name,
            int page,
            int size,
            String sortBy,
            String order
    );

    ApiResponse<DoctorResponse> getDoctorById(UUID doctorId);
    
    ApiResponse<DoctorResponse> getDoctorByUserId(UUID userId);
    
    ApiResponse<DoctorResponse> updateDoctor(UUID doctorId, DoctorRequest request, UUID authenticatedUserId);
    
    ApiResponse<EducationResponse> addEducation(UUID doctorId, EducationRequest request, UUID authenticatedUserId);
    
    ApiResponse<ExperienceResponse> addExperience(UUID doctorId, ExperienceRequest request, UUID authenticatedUserId);
    
    ApiResponse<String> removeEducation(UUID doctorId, Long educationId, UUID authenticatedUserId);
    
    ApiResponse<String> removeExperience(UUID doctorId, Long experienceId, UUID authenticatedUserId);

    ApiResponse<String> deleteDoctorById(UUID doctorId);

}

