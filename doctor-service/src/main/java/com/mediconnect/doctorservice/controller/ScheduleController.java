package com.mediconnect.doctorservice.controller;

import com.mediconnect.doctorservice.dto.requestDtos.ScheduleRequest;
import com.mediconnect.doctorservice.dto.responseDtos.ApiResponse;
import com.mediconnect.doctorservice.dto.responseDtos.ScheduleResponse;
import com.mediconnect.doctorservice.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ScheduleResponse>> addSchedule(
            @RequestBody ScheduleRequest request) {

        return ResponseEntity.ok(scheduleService.addSchedule(request));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getScheduleByDoctor(
            @PathVariable UUID doctorId
    ) {
        return ResponseEntity.ok(scheduleService.getScheduleByDoctor(doctorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequest request
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.deleteSchedule(id));
    }

}
