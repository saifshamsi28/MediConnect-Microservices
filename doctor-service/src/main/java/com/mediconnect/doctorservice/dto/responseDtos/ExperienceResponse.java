package com.mediconnect.doctorservice.dto.responseDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExperienceResponse {
    private Long id;
    private String hospitalName;
    private String role;
    private int years;
}
