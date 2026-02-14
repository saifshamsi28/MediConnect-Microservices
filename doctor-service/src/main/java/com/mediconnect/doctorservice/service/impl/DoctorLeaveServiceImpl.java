package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.LeaveRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.LeaveResponse;
import com.mediconnect.doctorservice.dto.responseDtos.Meta;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.entity.DoctorLeave;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.exception.InvalidRequestException;
import com.mediconnect.doctorservice.exception.ResourceNotFoundException;
import com.mediconnect.doctorservice.exception.UnauthorizedAccessException;
import com.mediconnect.doctorservice.repository.DoctorLeaveRepository;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.service.DoctorLeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorLeaveServiceImpl implements DoctorLeaveService {

    private final DoctorRepository doctorRepository;
    private final DoctorLeaveRepository leaveRepository;

    @Override
    @Transactional
    public ApiResponse<LeaveResponse> createLeave(UUID authenticatedUserId, LeaveRequest request) {

        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new InvalidRequestException("Start date and end date are required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidRequestException("Start date must be before or equal to end date");
        }

        Doctor doctor = doctorRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found for authenticated user"
                ));

        boolean hasOverlap = leaveRepository.existsByDoctorIdAndDateRangeOverlap(
                doctor.getId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (hasOverlap) {
            throw new InvalidRequestException(
                    "Leave dates overlap with existing leave period"
            );
        }

        DoctorLeave leave = new DoctorLeave();
        leave.setDoctor(doctor);
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());

        DoctorLeave saved = leaveRepository.save(leave);

        log.info("Leave created for doctor: {}, dates: {} to {}",
                doctor.getId(), saved.getStartDate(), saved.getEndDate());

        return ApiResponse.<LeaveResponse>builder()
                .success(true)
                .message("Leave created successfully")
                .data(mapToResponse(saved))
                .build();
    }

    @Override
    public ApiResponse<List<LeaveResponse>> getLeavesByDoctor(UUID doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + doctorId);
        }

        List<DoctorLeave> leaves = leaveRepository.findByDoctorId(doctorId);

        List<LeaveResponse> responses = leaves.stream()
                .map(this::mapToResponse)
                .toList();

        Meta meta = Meta.builder()
                .matched(leaves.size())
                .returned(responses.size())
                .build();

        return ApiResponse.<List<LeaveResponse>>builder()
                .success(true)
                .message("Leaves retrieved successfully")
                .data(responses)
                .meta(meta)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteLeave(UUID authenticatedUserId, Long leaveId) {

        DoctorLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leave not found with id: " + leaveId
                ));

        Doctor authenticatedDoctor = doctorRepository.findByUserId(authenticatedUserId)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found for authenticated user"
                ));

        if (!leave.getDoctor().getId().equals(authenticatedDoctor.getId())) {
            throw new UnauthorizedAccessException(
                    "You are not authorized to delete this leave"
            );
        }

        leaveRepository.delete(leave);

        log.info("Leave deleted: id={}, doctor={}", leaveId, authenticatedDoctor.getId());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Leave deleted successfully")
                .data("Deleted")
                .build();
    }

    private LeaveResponse mapToResponse(DoctorLeave leave) {
        return LeaveResponse.builder()
                .id(leave.getId())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .build();
    }
}