package com.mediconnect.appointmentservice.DTO.requestDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleRequest {

    private LocalDateTime newSlotStart;
    private LocalDateTime newSlotEnd;
}