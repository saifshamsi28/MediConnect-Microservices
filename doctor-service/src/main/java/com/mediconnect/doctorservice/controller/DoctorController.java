package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.DoctorRequest;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public Doctor createDoctor(@RequestBody DoctorRequest request) {
        return doctorService.createDoctor(request);
    }

    @PostMapping("/bulk")
    public List<Doctor> createDoctorsBulk(@RequestBody List<DoctorRequest> requests) {
        return doctorService.createDoctorsBulk(requests);
    }

    @GetMapping("/all-doctors")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

}

