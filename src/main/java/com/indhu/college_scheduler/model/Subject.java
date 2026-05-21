package com.indhu.college_scheduler.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String type; // THEORY or LAB

    @Column(nullable = false)
    private int hoursPerWeek; // auto: theory=4, lab=2
}