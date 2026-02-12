package com.mediconnect.doctorservice.dto.requestDtos;

import lombok.Data;

@Data
public class ExperienceRequest {
    private String hospitalName;
    private String role;
    private int years;
}
