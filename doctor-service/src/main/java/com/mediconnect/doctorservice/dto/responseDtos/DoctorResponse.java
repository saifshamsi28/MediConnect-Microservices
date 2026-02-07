package com.mediconnect.doctorservice.dto.responseDtos;

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
    private String primarySpecialization;
    private boolean active;
    private LocalDate dateOfJoining;
}
