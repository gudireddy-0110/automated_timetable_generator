package com.indhu.college_scheduler.repository;

import com.indhu.college_scheduler.model.FacultyAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacultyAssignmentRepository extends JpaRepository<FacultyAssignment, Long> {
    List<FacultyAssignment> findByBranchAndYear(String branch, int year);
    List<FacultyAssignment> findByFacultyName(String facultyName);
    List<FacultyAssignment> findByBranchAndYearAndSection(String branch, int year, String section);
}