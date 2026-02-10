package com.mediconnect.appointmentservice.DTO.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DoctorResponse {
    private UUID doctorId;
    private UUID userId;
    private String name;
    private String email;
    private String primarySpecialization;
    private boolean active;
}