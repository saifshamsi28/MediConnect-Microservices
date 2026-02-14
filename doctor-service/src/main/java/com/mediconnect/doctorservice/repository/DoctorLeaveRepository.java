package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.DoctorLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {

    List<DoctorLeave> findByDoctorId(UUID doctorId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM DoctorLeave l " +
           "WHERE l.doctor.id = :doctorId " +
           "AND ((l.startDate <= :endDate AND l.endDate >= :startDate))")
    boolean existsByDoctorIdAndDateRangeOverlap(
            @Param("doctorId") UUID doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}