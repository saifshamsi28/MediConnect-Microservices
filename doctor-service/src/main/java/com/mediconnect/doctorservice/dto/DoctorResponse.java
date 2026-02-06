package com.mediconnect.doctorservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class DoctorResponse {

    private UUID doctorId;
    private UUID userId;
    private String name;
    private String email;
    private String specialization;
    private Integer experienceYears;
    private boolean active;
    private LocalDate dateOfJoining;
}

