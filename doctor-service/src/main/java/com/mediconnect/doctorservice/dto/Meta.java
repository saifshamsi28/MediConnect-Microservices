package com.mediconnect.doctorservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {
    private int matched;
    private int returned;
    private int page;
    private int size;
    private int totalPages;
    private String sortBy;
    private String order;
    private Boolean active;
}
