package com.mediconnect.appointmentservice.DTO.responseDTO;

import com.mediconnect.appointmentservice.enums.AppointmentStatus;
import com.mediconnect.appointmentservice.enums.ConsultationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AppointmentResponse {

    private UUID appointmentId;

    private UUID doctorId;
    private UUID patientId;

    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;

    private AppointmentStatus status;
    private ConsultationType consultationType;

    private String reason;
    private boolean paid;
}