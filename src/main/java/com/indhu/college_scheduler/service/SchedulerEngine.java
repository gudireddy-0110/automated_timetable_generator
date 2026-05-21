package com.indhu.college_scheduler.service;

import com.indhu.college_scheduler.model.*;
import com.indhu.college_scheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SchedulerEngine {

    private static final String[] DAYS    = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private static final int PERIODS      = 6;
    private static final String[] ROOMS   = {"A101","A102","A103","B101","B102","B103","C101","C102","LAB1","LAB2","LAB3"};

    @Autowired private TimetableSlotRepository slotRepository;

    // Occupied maps — key: "day-period"
    private Map<String, Boolean> roomOccupied    = new HashMap<>();
    private Map<String, Boolean> facultyOccupied = new HashMap<>();
    private Map<String, Boolean> sectionOccupied = new HashMap<>();

    public List<TimetableSlot> generateForBranchYear(
            String branch, int year,
            List<FacultyAssignment> assignments,
            List<Subject> subjects) {

        // Clear existing slots for this branch+year
        slotRepository.deleteByBranchAndYear(branch, year);

        // Load existing occupied slots from DB (other branches already scheduled)
        roomOccupied    = new HashMap<>();
        facultyOccupied = new HashMap<>();
        sectionOccupied = new HashMap<>();

        List<TimetableSlot> existing = slotRepository.findAll();
        for (TimetableSlot slot : existing) {
            String key = slot.getDay() + "-" + slot.getPeriod();
            roomOccupied.put(slot.getRoomNumber() + "-" + key, true);
            facultyOccupied.put(slot.getFacultyName() + "-" + key, true);
            sectionOccupied.put(slot.getBranch() + "-" + slot.getYear() + "-" + slot.getSection() + "-" + key, true);
        }

        List<TimetableSlot> generated = new ArrayList<>();

        // Group assignments by section
        Map<String, List<FacultyAssignment>> bySection = new LinkedHashMap<>();
        for (FacultyAssignment fa : assignments) {
            bySection.computeIfAbsent(fa.getSection(), k -> new ArrayList<>()).add(fa);
        }

        for (Map.Entry<String, List<FacultyAssignment>> entry : bySection.entrySet()) {
            String section = entry.getKey();
            List<FacultyAssignment> sectionAssignments = entry.getValue();

            for (FacultyAssignment fa : sectionAssignments) {
                // Find subject hours
                int hoursNeeded = subjects.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(fa.getSubjectName()))
                        .mapToInt(Subject::getHoursPerWeek)
                        .findFirst().orElse(4);

                boolean isLab = fa.getSubjectName().toLowerCase().contains("lab");
                int assigned = 0;

                outer:
                for (String day : DAYS) {
                    for (int period = 1; period <= PERIODS; period++) {
                        if (assigned >= hoursNeeded) break outer;

                        String key         = day + "-" + period;
                        String sectionKey  = branch + "-" + year + "-" + section + "-" + key;
                        String facultyKey  = fa.getFacultyName() + "-" + key;

                        if (sectionOccupied.getOrDefault(sectionKey, false)) continue;
                        if (facultyOccupied.getOrDefault(facultyKey, false)) continue;

                        // Find available room
                        String availableRoom = null;
                        for (String room : ROOMS) {
                            if (isLab && !room.startsWith("LAB")) continue;
                            if (!isLab && room.startsWith("LAB")) continue;
                            String roomKey = room + "-" + key;
                            if (!roomOccupied.getOrDefault(roomKey, false)) {
                                availableRoom = room;
                                break;
                            }
                        }

                        if (availableRoom == null) continue;

                        // Assign slot
                        roomOccupied.put(availableRoom + "-" + key, true);
                        facultyOccupied.put(facultyKey, true);
                        sectionOccupied.put(sectionKey, true);

                        TimetableSlot slot = new TimetableSlot();
                        slot.setBranch(branch);
                        slot.setYear(year);
                        slot.setSection(section);
                        slot.setDay(day);
                        slot.setPeriod(period);
                        slot.setSubjectName(fa.getSubjectName());
                        slot.setFacultyName(fa.getFacultyName());
                        slot.setRoomNumber(availableRoom);
                        generated.add(slot);
                        assigned++;
                    }
                }
            }
        }

        return slotRepository.saveAll(generated);
    }
}