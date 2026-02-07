package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorId(UUID doctorId);

    Optional<DoctorSchedule> findByDoctorIdAndScheduleDate(UUID doctorId, LocalDate date);
}
