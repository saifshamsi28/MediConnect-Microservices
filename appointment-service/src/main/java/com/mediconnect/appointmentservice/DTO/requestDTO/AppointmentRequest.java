package com.mediconnect.appointmentservice.DTO.requestDTO;

import com.mediconnect.appointmentservice.enums.ConsultationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentRequest {

    private UUID doctorId;
    private UUID patientId;

    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;

    private ConsultationType consultationType;

    private String reason;
}