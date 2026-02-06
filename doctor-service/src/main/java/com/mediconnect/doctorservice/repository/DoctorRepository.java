package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByActiveTrue();

    Page<Doctor> findByActive(boolean active, Pageable pageable);
}

