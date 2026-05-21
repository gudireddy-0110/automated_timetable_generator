package com.indhu.college_scheduler.repository;

import com.indhu.college_scheduler.model.TimetableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TimetableSlotRepository extends JpaRepository<TimetableSlot, Long> {
    List<TimetableSlot> findByBranchAndYearAndSection(String branch, int year, String section);
    List<TimetableSlot> findByFacultyName(String facultyName);
    List<TimetableSlot> findByRoomNumber(String roomNumber);
    List<TimetableSlot> findByDayAndPeriod(String day, int period);
    void deleteByBranchAndYear(String branch, int year);
}