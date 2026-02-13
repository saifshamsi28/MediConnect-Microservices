package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.DoctorEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorEducationRepository extends JpaRepository<DoctorEducation, Long> {
    
    List<DoctorEducation> findByDoctorId(UUID doctorId);
    
    void deleteByDoctorIdAndId(UUID doctorId, Long id);
}
