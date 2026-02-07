package com.mediconnect.doctorservice.entity;

import com.mediconnect.doctorservice.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "doctor_availability",
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "day_of_week"})
)
@Getter
@Setter
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // working day of doctor like MONDAY, TUESDAY, etc.
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;


    // doctor consultation start time
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
