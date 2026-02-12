package com.mediconnect.doctorservice.dto.responseDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EducationResponse {
    private Long id;
    private String degree;
    private String institution;
    private int yearOfCompletion;
}
