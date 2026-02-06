package com.mediconnect.doctorservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DoctorRequest {

    private String name;
    private String email;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private boolean active = true;
    private LocalDate dateOfJoining;
}
