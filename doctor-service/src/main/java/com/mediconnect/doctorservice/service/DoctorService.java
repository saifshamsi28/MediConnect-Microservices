package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.DoctorRequest;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.exception.DoctorAlreadyExistsException;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor with email \"" + request.getEmail() + "\" already exists!"
            );
        }

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .email(request.getEmail())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .active(request.isActive())
                .dateOfJoining(request.getDateOfJoining())
                .build();

        return doctorRepository.save(doctor);
    }

    public List<Doctor> createDoctorsBulk(List<DoctorRequest> requests) {
        List<Doctor> doctors = requests.stream().map(req -> {

            if (doctorRepository.existsByEmail(req.getEmail())) {
                throw new DoctorAlreadyExistsException(
                        "Duplicate email found: " + req.getEmail()
                );
            }

            return Doctor.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .specialization(req.getSpecialization())
                    .qualification(req.getQualification())
                    .experienceYears(req.getExperienceYears())
                    .active(req.isActive())
                    .dateOfJoining(req.getDateOfJoining())
                    .build();

        }).collect(Collectors.toList());

        return doctorRepository.saveAll(doctors);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

}
