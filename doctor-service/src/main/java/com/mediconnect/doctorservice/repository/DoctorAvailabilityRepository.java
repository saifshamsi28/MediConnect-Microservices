package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.DoctorAvailability;
import com.mediconnect.doctorservice.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorId(UUID doctorId);

    Optional<DoctorAvailability> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);

}
