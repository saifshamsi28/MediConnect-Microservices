package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.DoctorExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorExperienceRepository extends JpaRepository<DoctorExperience, Long> {
    
    List<DoctorExperience> findByDoctorId(UUID doctorId);
    
    void deleteByDoctorIdAndId(UUID doctorId, Long id);
}
