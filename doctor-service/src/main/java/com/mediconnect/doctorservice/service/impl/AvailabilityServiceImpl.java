package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.AvailabilityRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.AvailabilityResponse;
import com.mediconnect.doctorservice.dto.responseDtos.Meta;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.entity.DoctorAvailability;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.exception.InvalidAvailabilityException;
import com.mediconnect.doctorservice.repository.DoctorAvailabilityRepository;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;


    @Override
    public ApiResponse<AvailabilityResponse> addAvailability(AvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new DoctorNotFoundException("Doctor not found with id: " + request.getDoctorId())
                );

        // Validate time range
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidAvailabilityException("Start time must be before end time");
        }

        Optional<DoctorAvailability> existing =
                availabilityRepository.findByDoctor_IdAndDayOfWeek(
                        request.getDoctorId(), request.getDayOfWeek()
                );

        DoctorAvailability availability;

        if (existing.isPresent()) {
            availability = existing.get();
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
            availability.setAvailable(request.isAvailable());
        } else {
            availability = new DoctorAvailability();
            availability.setDoctor(doctor);
            availability.setDayOfWeek(request.getDayOfWeek());
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
            availability.setAvailable(request.isAvailable());
        }

        DoctorAvailability saved = availabilityRepository.save(availability);

        return ApiResponse.<AvailabilityResponse>builder()
                .data(toResponse(saved))
                .message("Availability created successfully")
                .meta(null)
                .build();
    }


    @Override
    public ApiResponse<List<AvailabilityResponse>> getAvailabilityByDoctor(UUID doctorId) {

        if(!doctorRepository.existsById(doctorId)){
            log.error("Doctor not found with id: {}",doctorId);
            throw new DoctorNotFoundException("Invalid doctor id: "+doctorId);
        }

        List<DoctorAvailability> list =
                availabilityRepository.findByDoctor_Id(doctorId);

//        log.info("Fetched availability: {}",list.getFirst().getDayOfWeek());

        List<AvailabilityResponse> availabilityResponses=list.stream()
                .map(this::toResponse)
                .toList();

        Meta meta = Meta.builder()
                .matched(list.size())
                .returned(availabilityResponses.size())
                .page(0)
                .size(10)
                .totalPages(10)
                .sortBy(null)
                .order(null)
                .active(true)
                .build();

        return ApiResponse.<List<AvailabilityResponse>>builder()
                .success(true)
                .message("All availability fetch for doctor")
                .meta(meta)
                .data(availabilityResponses)
                .build();
    }

    @Override
    public AvailabilityResponse updateAvailability(Long availabilityId,
                                                   AvailabilityRequest request) {

        DoctorAvailability availability =
                availabilityRepository.findById(availabilityId)
                        .orElseThrow(() ->
                                new InvalidAvailabilityException("Availability not found with id: " + availabilityId)
                        );

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidAvailabilityException("Start time must be before end time");
        }

        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.isAvailable());

        DoctorAvailability updated = availabilityRepository.save(availability);
        return toResponse(updated);
    }

    @Override
    public void deleteAvailability(Long availabilityId) {
        Optional<DoctorAvailability> availability=availabilityRepository.findById(availabilityId);
        if (availability.isEmpty()){
            throw new InvalidAvailabilityException("No availability found with id: "+availabilityId);
        }
        availabilityRepository.deleteById(availabilityId);
    }

    private AvailabilityResponse toResponse(DoctorAvailability a) {
        return AvailabilityResponse.builder()
                .availabilityId(a.getId())
                .doctorId(a.getDoctor().getId())
                .dayOfWeek(a.getDayOfWeek())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .available(a.isAvailable())
                .build();
    }
}
