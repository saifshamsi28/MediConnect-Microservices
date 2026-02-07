package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.DoctorResponse;
import com.mediconnect.doctorservice.dto.responseDtos.Meta;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.exception.DoctorAlreadyExistsException;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor with email '" + request.getEmail() + "' already exists!"
            );
        }

        Doctor doctor = Doctor.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .primarySpecialization(request.getPrimarySpecialization())
                .active(request.isActive())
                .dateOfJoining(request.getDateOfJoining())
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        return toDoctorResponse(savedDoctor);
    }

    @Override
    public List<DoctorResponse> createDoctorsBulk(List<DoctorRequest> requests) {

        List<Doctor> doctors = requests.stream().map(req -> {

            if (doctorRepository.existsByEmail(req.getEmail())) {
                throw new DoctorAlreadyExistsException(
                        "Duplicate email found: " + req.getEmail()
                );
            }

            return Doctor.builder()
                    .userId(req.getUserId())
                    .name(req.getName())
                    .email(req.getEmail())
                    .primarySpecialization(req.getPrimarySpecialization())
                    .active(req.isActive())
                    .dateOfJoining(req.getDateOfJoining())
                    .build();

        }).collect(Collectors.toList());

        List<Doctor> savedDoctors = doctorRepository.saveAll(doctors);

        return savedDoctors.stream()
                .map(this::toDoctorResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<List<DoctorResponse>> getDoctorsByFilters(
            Boolean active,
            int page,
            int size,
            String sortBy,
            String order) {

        Sort sort = order.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage;

        if (active == null) {
            doctorPage = doctorRepository.findAll(pageable);
        } else {
            doctorPage = doctorRepository.findByActive(active, pageable);
        }

        List<DoctorResponse> responses = doctorPage.getContent()
                .stream()
                .map(this::toDoctorResponse)
                .toList();

        Meta meta = Meta.builder()
                .matched((int) doctorPage.getTotalElements())
                .returned(responses.size())
                .page(page)
                .size(size)
                .totalPages(doctorPage.getTotalPages())
                .sortBy(sortBy)
                .order(order)
                .active(active)
                .build();

        return ApiResponse.<List<DoctorResponse>>builder()
                .success(true)
                .message("Doctors retrieved successfully")
                .data(responses)
                .meta(meta)
                .build();
    }

    @Override
    public DoctorResponse getDoctorById(UUID doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException("Doctor not found with id: " + doctorId)
                );

        return toDoctorResponse(doctor);
    }

    private DoctorResponse toDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .userId(doctor.getUserId())
                .doctorId(doctor.getId())
                .active(doctor.isActive())
                .primarySpecialization(doctor.getPrimarySpecialization())
                .dateOfJoining(doctor.getDateOfJoining())
                .email(doctor.getEmail())
                .name(doctor.getName())
                .build();
    }
}
