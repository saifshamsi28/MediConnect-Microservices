package com.mediconnect.appointmentservice.DTO.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class AvailabilityResponse {

    private Long availabilityId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}