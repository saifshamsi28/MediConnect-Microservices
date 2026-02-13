package com.mediconnect.userservice.client;

import com.mediconnect.userservice.dto.DoctorRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @PostMapping("/api/v1/doctors")
    void createDoctor(@RequestBody DoctorRequest request);
}
