package com.mediconnect.doctorservice.dto.requestDtos;

import lombok.Data;

@Data
public class EducationRequest {
    private String degree;
    private String institution;
    private int yearOfCompletion;
}
