package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.ScheduleRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.Meta;
import com.mediconnect.doctorservice.dto.responseDtos.ScheduleResponse;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.entity.DoctorSchedule;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.exception.InvalidScheduleException;
import com.mediconnect.doctorservice.exception.ScheduleNotFoundException;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.repository.DoctorScheduleRepository;
import com.mediconnect.doctorservice.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;

    @Override
    public ApiResponse<ScheduleResponse> addSchedule(ScheduleRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with id: " + request.getDoctorId()
                ));

        scheduleRepository.findByDoctorIdAndScheduleDate(
                request.getDoctorId(),
                request.getScheduleDate()
        ).ifPresent(existing -> {
            throw new InvalidScheduleException(
                    "Schedule already exists for this doctor on date: " + request.getScheduleDate()
            );
        });

        if (request.getStartTime() != null &&
                request.getEndTime() != null &&
                request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidScheduleException("Start time must be before end time");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .scheduleDate(request.getScheduleDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .working(request.isWorking())
                .build();

        DoctorSchedule saved = scheduleRepository.save(schedule);

        ScheduleResponse response = mapToResponse(saved);

        return ApiResponse.<ScheduleResponse>builder()
                .success(true)
                .message("Schedule created successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<List<ScheduleResponse>> getScheduleByDoctor(UUID doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + doctorId);
        }

        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorId(doctorId);

        List<ScheduleResponse> responses = schedules.stream()
                .map(this::mapToResponse)
                .toList();

        Meta meta = Meta.builder()
                .matched(schedules.size())
                .returned(responses.size())
                .build();

        return ApiResponse.<List<ScheduleResponse>>builder()
                .success(true)
                .message("Schedules retrieved successfully")
                .data(responses)
                .meta(meta)
                .build();
    }

    @Override
    public ApiResponse<ScheduleResponse> updateSchedule(Long id, ScheduleRequest request) {

        DoctorSchedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(
                        "Schedule not found with id: " + id
                ));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with id: " + request.getDoctorId()
                ));

        if (request.getStartTime() != null &&
                request.getEndTime() != null &&
                request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidScheduleException("Start time must be before end time");
        }

        DoctorSchedule updated = DoctorSchedule.builder()
                .id(existing.getId())
                .doctor(doctor)
                .scheduleDate(request.getScheduleDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .working(request.isWorking())
                .build();

        DoctorSchedule saved = scheduleRepository.save(updated);

        ScheduleResponse response = mapToResponse(saved);

        return ApiResponse.<ScheduleResponse>builder()
                .success(true)
                .message("Schedule updated successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<String> deleteSchedule(Long id) {

        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException(
                    "Schedule not found with id: " + id
            );
        }

        scheduleRepository.deleteById(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Schedule deleted successfully")
                .data("Deleted")
                .build();
    }


    private ScheduleResponse mapToResponse(DoctorSchedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .working(schedule.isWorking())
                .build();
    }
}

