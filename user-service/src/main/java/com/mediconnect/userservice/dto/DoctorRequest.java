package com.mediconnect.userservice.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DoctorRequest {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String primarySpecialization;
    private boolean active = false;
    private LocalDate dateOfJoining;
}

