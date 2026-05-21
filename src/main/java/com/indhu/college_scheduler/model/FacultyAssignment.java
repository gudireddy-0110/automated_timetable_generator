package com.indhu.college_scheduler.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "faculty_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String facultyName;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String section; // A, B, C
}