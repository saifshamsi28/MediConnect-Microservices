package com.mediconnect.appointmentservice.service.impl;

import com.mediconnect.appointmentservice.DTO.requestDTO.AppointmentRequest;
import com.mediconnect.appointmentservice.DTO.requestDTO.RescheduleRequest;
import com.mediconnect.appointmentservice.DTO.responseDTO.AppointmentResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.AvailabilityResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.DoctorResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.LeaveResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.ScheduleResponse;
import com.mediconnect.appointmentservice.DTO.responseDTO.SlotResponse;
import com.mediconnect.appointmentservice.client.DoctorClient;
import com.mediconnect.appointmentservice.entity.Appointment;
import com.mediconnect.appointmentservice.enums.AppointmentStatus;
import com.mediconnect.appointmentservice.exception.AppointmentNotFoundException;
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
import java.util.Optional;
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

                // Validate slot is available (checks leave, schedule, and working hours)
                if (!isSlotValid(request.getDoctorId(), request.getSlotStart())) {
                        throw new DoctorNotAvailableException("Slot is not available");
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

                log.info("Upcoming request: {}",doctorId + " and "+ date);

                LocalDate selectedDate = LocalDate.parse(date);

                // Check if doctor is on leave
                if (!isDoctorAvailable(doctorId, selectedDate)) {
                        log.info("Doctor {} is on leave on {}", doctorId, selectedDate);
                        throw new RuntimeException("Doctor is not available on " + selectedDate + ". The doctor is on leave.");
                }

                // Get effective working hours
                TimeRange timeRange = getEffectiveWorkingHours(doctorId, selectedDate);

                if (timeRange == null) {
                        log.info("Doctor {} has no working hours set for {}", doctorId, selectedDate);
                        throw new RuntimeException("Doctor is not available on " + selectedDate + ". No working hours configured for this day.");
                }

                // Generate time slots
                List<SlotResponse> generatedSlots = generateTimeSlots(
                        selectedDate,
                        timeRange.startTime,
                        timeRange.endTime,
                        30
                );

                // Filter out booked slots
                Set<LocalDateTime> bookedStartTimes = getBookedSlots(
                        doctorId,
                        selectedDate,
                        timeRange.startTime,
                        timeRange.endTime
                );

                List<SlotResponse> availableSlots = generatedSlots.stream()
                        .filter(slot -> !bookedStartTimes.contains(slot.getSlotStart()))
                        .toList();

                // If all slots are booked, provide a clear message
                if (availableSlots.isEmpty() && !generatedSlots.isEmpty()) {
                        log.info("All slots are booked for doctor {} on {}", doctorId, selectedDate);
                        throw new RuntimeException("All appointment slots are fully booked for " + selectedDate + ". Please try another date.");
                }

                return availableSlots;
        }

        private boolean isDoctorAvailable(UUID doctorId, LocalDate selectedDate) {

                try {
                        ApiResponse<List<LeaveResponse>> leavesResponse = doctorClient.getLeaves(doctorId);

                        if (leavesResponse != null && leavesResponse.getData() != null) {
                                boolean isOnLeave = leavesResponse.getData().stream()
                                        .anyMatch(leave ->
                                                !selectedDate.isBefore(leave.getStartDate()) &&
                                                        !selectedDate.isAfter(leave.getEndDate())
                                        );

                                if (isOnLeave) {
                                        log.info("Doctor {} is on leave on {}", doctorId, selectedDate);
                                        return false;
                                }
                        }
                } catch (Exception e) {
                        log.warn("Failed to fetch leaves for doctor {}: {}", doctorId, e.getMessage());
                }

                return true;
        }

        private TimeRange getEffectiveWorkingHours(UUID doctorId, LocalDate selectedDate) {

                try {
                        ApiResponse<List<ScheduleResponse>> schedulesResponse = doctorClient.getSchedules(doctorId);

                        if (schedulesResponse != null && schedulesResponse.getData() != null) {
                                Optional<ScheduleResponse> scheduleOpt = schedulesResponse.getData().stream()
                                        .filter(s -> s.getScheduleDate().equals(selectedDate))
                                        .findFirst();

                                if (scheduleOpt.isPresent()) {
                                        ScheduleResponse schedule = scheduleOpt.get();

                                        if (!schedule.isWorking()) {
                                                log.info("Doctor {} not working on {} (schedule override)", doctorId, selectedDate);
                                                return null;
                                        }

                                        log.info("Using schedule override for doctor {} on {}", doctorId, selectedDate);
                                        return new TimeRange(schedule.getStartTime(), schedule.getEndTime());
                                }
                        }
                } catch (Exception e) {
                        log.warn("Failed to fetch schedules for doctor {}: {}", doctorId, e.getMessage());
                }

                return getWeeklyAvailabilityHours(doctorId, selectedDate);
        }

        private TimeRange getWeeklyAvailabilityHours(UUID doctorId, LocalDate selectedDate) {

                java.time.DayOfWeek javaDay = selectedDate.getDayOfWeek();

                try {
                        ApiResponse<List<AvailabilityResponse>> response = doctorClient.getAvailability(doctorId);

                        if (response == null || response.getData() == null) {
                                log.warn("No availability data for doctor {}", doctorId);
                                return null;
                        }

                        Optional<AvailabilityResponse> availabilityOpt = response.getData().stream()
                                .filter(a -> a.getDayOfWeek().name().equals(javaDay.name()))
                                .findFirst();

                        if (availabilityOpt.isEmpty()) {
                                log.info("Doctor {} not available on {}", doctorId, javaDay);
                                return null;
                        }

                        AvailabilityResponse availability = availabilityOpt.get();

                        if (!availability.isAvailable()) {
                                log.info("Doctor {} marked unavailable on {}", doctorId, javaDay);
                                return null;
                        }

                        return new TimeRange(availability.getStartTime(), availability.getEndTime());

                } catch (Exception e) {
                        log.error("Error fetching weekly availability for doctor {}: {}", doctorId, e.getMessage());
                        return null;
                }
        }

        private List<SlotResponse> generateTimeSlots(
                LocalDate date,
                LocalTime startTime,
                LocalTime endTime,
                int slotMinutes) {

                List<SlotResponse> slots = new ArrayList<>();

                LocalDateTime slotStart = LocalDateTime.of(date, startTime);
                LocalDateTime slotEnd = slotStart.plusMinutes(slotMinutes);
                LocalDateTime dayEnd = LocalDateTime.of(date, endTime);

                while (!slotEnd.isAfter(dayEnd)) {
                        slots.add(SlotResponse.builder()
                                .slotStart(slotStart)
                                .slotEnd(slotEnd)
                                .build());

                        slotStart = slotStart.plusMinutes(slotMinutes);
                        slotEnd = slotEnd.plusMinutes(slotMinutes);
                }

                return slots;
        }

        private Set<LocalDateTime> getBookedSlots(
                UUID doctorId,
                LocalDate date,
                LocalTime startTime,
                LocalTime endTime) {

                List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndSlotStartBetween(
                        doctorId,
                        LocalDateTime.of(date, startTime),
                        LocalDateTime.of(date, endTime)
                );

                return bookedAppointments.stream()
                        .map(Appointment::getSlotStart)
                        .collect(Collectors.toSet());
        }

        private boolean isSlotValid(UUID doctorId, LocalDateTime slotStart) {
                LocalDate date = slotStart.toLocalDate();
                String dateString = date.toString();

                try {
                        List<SlotResponse> availableSlots = getAvailableSlots(doctorId, dateString);

                        return availableSlots.stream()
                                .anyMatch(slot -> slot.getSlotStart().equals(slotStart));

                } catch (Exception e) {
                        log.error("Error validating slot for doctor {}: {}", doctorId, e.getMessage());
                        return false;
                }
        }

        private static class TimeRange {
                LocalTime startTime;
                LocalTime endTime;

                TimeRange(LocalTime startTime, LocalTime endTime) {
                        this.startTime = startTime;
                        this.endTime = endTime;
                }
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

        @Override
        public AppointmentResponse cancelByAppointmentId(UUID id) {
                Appointment appointment=appointmentRepository.findById(id)
                        .orElseThrow(()-> new AppointmentNotFoundException("No appointment found with id"));

                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointmentRepository.save(appointment);

                return mapToResponse(appointment);
        }



        @Override
        public AppointmentResponse rescheduleAppointment(UUID appointmentId, RescheduleRequest request) {

                Appointment appointment = appointmentRepository.findById(appointmentId)
                        .orElseThrow(() -> new AppointmentNotFoundException("No appointment found with id: " + appointmentId));

                // Only booked appointment can be rescheduled
                if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                        throw new RuntimeException("Cancelled appointment cannot be rescheduled");
                }

                // Validate slot is available (checks leave, schedule, and working hours)
                if (!isSlotValid(appointment.getDoctorId(), request.getNewSlotStart())) {
                        throw new DoctorNotAvailableException("Slot is not available");
                }

                // Check if slot already booked for same doctor
                boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndSlotStart(
                        appointment.getDoctorId(),
                        request.getNewSlotStart()
                );

                if (alreadyBooked) {
                        throw new DoctorNotAvailableException("Slot already booked!");
                }

                // Update slot only
                appointment.setSlotStart(request.getNewSlotStart());
                appointment.setSlotEnd(request.getNewSlotEnd());

                Appointment saved = appointmentRepository.save(appointment);

                return mapToResponse(saved);
        }


}