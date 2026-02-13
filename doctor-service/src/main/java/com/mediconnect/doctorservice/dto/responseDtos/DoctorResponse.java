package com.mediconnect.doctorservice.dto.responseDtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DoctorResponse {

    private UUID doctorId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String primarySpecialization;
    private boolean active;
    private LocalDate dateOfJoining;
    
    // Profile completion data
    private List<EducationResponse> educationList;
    private List<ExperienceResponse> experienceList;
    private List<AvailabilityResponse> availabilityList;
    
    // Computed flag for profile completeness
    private boolean profileComplete;
}

