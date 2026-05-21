package com.indhu.college_scheduler.service;

import com.indhu.college_scheduler.model.*;
import com.indhu.college_scheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimetableService {

    @Autowired private BranchRepository branchRepository;
    @Autowired private FacultyAssignmentRepository assignmentRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private TimetableSlotRepository slotRepository;
    @Autowired private SchedulerEngine schedulerEngine;

    public Map<String, Object> generateAll() {
        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) throw new RuntimeException("No branches found. Please upload CSV first.");

        int totalSlots = 0;
        List<String> generated = new ArrayList<>();

        for (Branch branch : branches) {
            for (int year = 1; year <= 4; year++) {
                List<FacultyAssignment> assignments = assignmentRepository.findByBranchAndYear(branch.getName(), year);
                if (assignments.isEmpty()) continue;

                List<Subject> subjects = subjectRepository.findByBranchAndYear(branch.getName(), year);
                List<TimetableSlot> slots = schedulerEngine.generateForBranchYear(branch.getName(), year, assignments, subjects);
                totalSlots += slots.size();
                generated.add(branch.getName() + " Year " + year + " → " + slots.size() + " slots");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalSlots", totalSlots);
        result.put("generated", generated);
        return result;
    }

    public List<TimetableSlot> getSectionTimetable(String branch, int year, String section) {
        return slotRepository.findByBranchAndYearAndSection(branch.toUpperCase(), year, section.toUpperCase());
    }

    public List<TimetableSlot> getFacultyTimetable(String facultyName) {
        return slotRepository.findByFacultyName(facultyName);
    }

    public List<TimetableSlot> getRoomUtilization(String roomNumber) {
        return slotRepository.findByRoomNumber(roomNumber);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSlots", slotRepository.count());
        stats.put("totalFacultyAssignments", assignmentRepository.count());
        stats.put("totalBranches", branchRepository.count());
        stats.put("totalSubjects", subjectRepository.count());
        return stats;
    }
}