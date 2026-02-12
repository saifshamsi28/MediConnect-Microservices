package com.mediconnect.appointmentservice.service.impl;

import com.mediconnect.appointmentservice.DTO.requestDTO.AppointmentRequest;
import com.mediconnect.appointmentservice.DTO.responseDTO.AppointmentResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.AvailabilityResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.DoctorResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.SlotResponse;
import com.mediconnect.appointmentservice.client.DoctorClient;
import com.mediconnect.appointmentservice.entity.Appointment;
import com.mediconnect.appointmentservice.enums.AppointmentStatus;
import com.mediconnect.appointmentservice.exception.DoctorNotAvailableException;
import com.mediconnect.appointmentservice.repository.AppointmentRepository;
import com.mediconnect.appointmentservice.service.AppointmentService;

import com.mediconnect.appointmentservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final DoctorClient doctorClient;

        @Override
        public AppointmentResponse bookAppointment(AppointmentRequest request) {

                ApiResponse<DoctorResponse> doctorApiResponse = doctorClient.getDoctorById(request.getDoctorId());

                if (doctorApiResponse == null || doctorApiResponse.getData() == null) {
                        throw new DoctorNotAvailableException("Doctor not found!");
                }

                DoctorResponse doctor = doctorApiResponse.getData();

                if (!doctor.isActive()) {
                        throw new DoctorNotAvailableException("Doctor is not active currently");
                }

                boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndSlotStart(
                                request.getDoctorId(),
                                request.getSlotStart());

                if (alreadyBooked) {
                        throw new DoctorNotAvailableException("Slot already booked!");
                }

                Appointment appointment = Appointment.builder()
                                .doctorId(request.getDoctorId())
                                .patientId(request.getPatientId())
                                .slotStart(request.getSlotStart())
                                .slotEnd(request.getSlotEnd())
                                .consultationType(request.getConsultationType())
                                .status(AppointmentStatus.BOOKED)
                                .reason(request.getReason())
                                .paid(false)
                                .build();

                Appointment saved = appointmentRepository.save(appointment);

                return mapToResponse(saved);
        }

        private AppointmentResponse mapToResponse(Appointment appointment) {
                return AppointmentResponse.builder()
                                .appointmentId(appointment.getId())
                                .doctorId(appointment.getDoctorId())
                                .patientId(appointment.getPatientId())
                                .slotStart(appointment.getSlotStart())
                                .slotEnd(appointment.getSlotEnd())
                                .status(appointment.getStatus())
                                .consultationType(appointment.getConsultationType())
                                .reason(appointment.getReason())
                                .paid(appointment.isPaid())
                                .build();
        }

        @Override
        public List<SlotResponse> getAvailableSlots(UUID doctorId, String date) {

                LocalDate selectedDate = LocalDate.parse(date);

                // day name nikalna (MONDAY, TUESDAY)
                java.time.DayOfWeek javaDay = selectedDate.getDayOfWeek();

                log.info("Parsed day: {}", javaDay);

                // doctor-service call
                ApiResponse<List<AvailabilityResponse>> response = doctorClient.getAvailability(doctorId);

                // log.info("Fetched availability:
                // {}",response.getData().getFirst().getAvailabilityId());
                // log.info("Fetched availability:
                // {}",response.getData().getFirst().getDayOfWeek());
                // log.info("Fetched availability:
                // {}",response.getData().getFirst().getStartTime());
                // log.info("Fetched availability:
                // {}",response.getData().getFirst().getEndTime());
                // log.info("Fetched availability: {}",response.getData().getFirst().get());
                // log.info("Fetched availability:
                // {}",response.getData().getFirst().getDayOfWeek());

                if (response.getData() == null) {
                        throw new RuntimeException("No availability found for doctor");
                }

                // filter by selected day
                AvailabilityResponse availability = response.getData().stream()
                                .filter(a -> a.getDayOfWeek().name().equals(javaDay.name()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Doctor not available on " + javaDay));

                if (!availability.isAvailable()) {
                        throw new RuntimeException("Doctor is not available on " + javaDay);
                }

                LocalTime startTime = availability.getStartTime();
                LocalTime endTime = availability.getEndTime();

                // slot duration (example 30 minutes)
                int slotMinutes = 30;

                List<SlotResponse> generatedSlots = new ArrayList<>();

                LocalDateTime slotStart = LocalDateTime.of(selectedDate, startTime);
                LocalDateTime slotEnd = slotStart.plusMinutes(slotMinutes);

                while (!slotEnd.isAfter(LocalDateTime.of(selectedDate, endTime))) {

                        generatedSlots.add(SlotResponse.builder()
                                        .slotStart(slotStart)
                                        .slotEnd(slotEnd)
                                        .build());

                        slotStart = slotStart.plusMinutes(slotMinutes);
                        slotEnd = slotEnd.plusMinutes(slotMinutes);
                }

                // Remove already booked slots
                List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndSlotStartBetween(
                                doctorId,
                                LocalDateTime.of(selectedDate, startTime),
                                LocalDateTime.of(selectedDate, endTime));

                Set<LocalDateTime> bookedStartTimes = bookedAppointments.stream()
                                .map(Appointment::getSlotStart)
                                .collect(Collectors.toSet());

                return generatedSlots.stream()
                                .filter(slot -> !bookedStartTimes.contains(slot.getSlotStart()))
                                .toList();
        }

        @Override
        public List<AppointmentResponse> getAppointmentsByPatient(UUID patientId) {
                List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
                return appointments.stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Override
        public List<AppointmentResponse> getAppointmentsByDoctor(UUID doctorId) {
                List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
                return appointments.stream()
                                .map(this::mapToResponse)
                                .toList();
        }

}