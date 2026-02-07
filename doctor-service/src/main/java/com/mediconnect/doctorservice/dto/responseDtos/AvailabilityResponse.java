package com.mediconnect.doctorservice.dto.responseDtos;

import com.mediconnect.doctorservice.enums.DayOfWeek;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class AvailabilityResponse {

    private Long availabilityId;
    private UUID doctorId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}
