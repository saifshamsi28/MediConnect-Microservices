package com.mediconnect.appointmentservice.DTO.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SlotResponse {

    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
}