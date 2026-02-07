package com.mediconnect.doctorservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedule")
@Getter
@Setter
public class DoctorSchedule {
    //Sometimes a doctor’s regular schedule changes for a particular date. so this class
    //will help to override

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // specific date override
    @Column(nullable = false)
    private LocalDate scheduleDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean working;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
