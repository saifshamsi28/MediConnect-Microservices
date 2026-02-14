package com.mediconnect.appointmentservice.repository;

import com.mediconnect.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByDoctorIdAndSlotStart(UUID doctorId, LocalDateTime slotStart);

    List<Appointment> findByDoctorId(UUID doctorId);

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByDoctorIdAndSlotStartBetween(
            UUID doctorId,
            LocalDateTime start,
            LocalDateTime end);
}