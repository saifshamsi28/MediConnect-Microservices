package com.mediconnect.doctorservice.dto.requestDtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DoctorRequest {

    private UUID userId;
    private String name;
    private String email;
    private String primarySpecialization;
    private boolean active = false;
    private LocalDate dateOfJoining;
}

