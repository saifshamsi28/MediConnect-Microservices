package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.AvailabilityRequest;
import com.mediconnect.doctorservice.dto.responseDtos.AvailabilityResponse;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.entity.DoctorAvailability;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.exception.InvalidAvailabilityException;
import com.mediconnect.doctorservice.repository.DoctorAvailabilityRepository;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;


    @Override
    public AvailabilityResponse addAvailability(AvailabilityRequest request) {

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new DoctorNotFoundException("Doctor not found with id: " + request.getDoctorId())
                );

        // Validate time range
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidAvailabilityException("Start time must be before end time");
        }

        Optional<DoctorAvailability> existing =
                availabilityRepository.findByDoctorIdAndDayOfWeek(
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
        return toResponse(saved);
    }


    @Override
    public List<AvailabilityResponse> getAvailabilityByDoctor(UUID doctorId) {

        if(!doctorRepository.existsById(doctorId)){
            throw new DoctorNotFoundException("Invalid doctor id: "+doctorId);
        }

        List<DoctorAvailability> list =
                availabilityRepository.findByDoctorId(doctorId);

        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
