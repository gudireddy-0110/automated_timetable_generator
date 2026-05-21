package com.indhu.college_scheduler.repository;

import com.indhu.college_scheduler.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByBranchAndYear(String branch, int year);
}