package com.mediconnect.doctorservice.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DoctorRequest {

    private UUID userId;
    private String name;
    private String email;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private boolean active = false;
    private LocalDate dateOfJoining;
}
