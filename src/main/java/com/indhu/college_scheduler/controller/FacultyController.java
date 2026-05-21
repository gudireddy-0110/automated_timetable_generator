package com.indhu.college_scheduler.controller;

import com.indhu.college_scheduler.model.Faculty;
import com.indhu.college_scheduler.model.FacultyAssignment;
import com.indhu.college_scheduler.repository.FacultyAssignmentRepository;
import com.indhu.college_scheduler.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    @Autowired private FacultyRepository facultyRepository;
    @Autowired private FacultyAssignmentRepository assignmentRepository;

    @GetMapping
    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facultyRepository.deleteById(id);
    }

    @GetMapping("/assignments")
    public List<FacultyAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @PostMapping("/assignments")
    public FacultyAssignment addAssignment(@RequestBody FacultyAssignment assignment) {
        assignment.setBranch(assignment.getBranch().toUpperCase());
        assignment.setSection(assignment.getSection().toUpperCase());
        return assignmentRepository.save(assignment);
    }

    @DeleteMapping("/assignments/{id}")
    public void deleteAssignment(@PathVariable Long id) {
        assignmentRepository.deleteById(id);
    }
}