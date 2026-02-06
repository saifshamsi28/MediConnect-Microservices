package com.mediconnect.doctorservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
@Builder
public class Doctor {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String specialization;

    private String qualification;

    private Integer experienceYears;

    @Column(nullable = false)
    private boolean active = true;

    private LocalDate dateOfJoining;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorAvailability> availabilityList;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorLeave> leaveList;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationSlot> consultationSlots;

    // runs only first time before saving entity and save the creation timestamp
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // run automatically everytime doctor entity will be updated
    // before updating entity hibernate will call this method and update the timestamp
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
