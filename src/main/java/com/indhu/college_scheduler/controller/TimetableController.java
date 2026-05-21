package com.indhu.college_scheduler.controller;

import com.indhu.college_scheduler.model.TimetableSlot;
import com.indhu.college_scheduler.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired private TimetableService timetableService;

    @PostMapping("/generate")
    public Map<String, Object> generate() {
        return timetableService.generateAll();
    }

    @GetMapping("/section")
    public List<TimetableSlot> getSectionTimetable(
            @RequestParam String branch,
            @RequestParam int year,
            @RequestParam String section) {
        return timetableService.getSectionTimetable(branch, year, section);
    }

    @GetMapping("/faculty")
    public List<TimetableSlot> getFacultyTimetable(@RequestParam String name) {
        return timetableService.getFacultyTimetable(name);
    }

    @GetMapping("/room")
    public List<TimetableSlot> getRoomUtilization(@RequestParam String room) {
        return timetableService.getRoomUtilization(room);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return timetableService.getStats();
    }
}