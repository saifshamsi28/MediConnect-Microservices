package com.mediconnect.doctorservice.dto.requestDtos;

import com.mediconnect.doctorservice.enums.DayOfWeek;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class AvailabilityRequest {

    private UUID doctorId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}
