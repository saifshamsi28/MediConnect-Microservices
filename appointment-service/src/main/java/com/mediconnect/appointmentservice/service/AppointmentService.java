package com.mediconnect.appointmentservice.service;

import com.mediconnect.appointmentservice.DTO.requestDTO.AppointmentRequest;
import com.mediconnect.appointmentservice.DTO.responseDTO.AppointmentResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.SlotResponse;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentRequest request);

    List<SlotResponse> getAvailableSlots(UUID doctorId, String date);

    List<AppointmentResponse> getAppointmentsByPatient(UUID patientId);

    List<AppointmentResponse> getAppointmentsByDoctor(UUID doctorId);
}