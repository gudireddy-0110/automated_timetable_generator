package com.indhu.college_scheduler.service;

import com.indhu.college_scheduler.model.*;
import com.indhu.college_scheduler.repository.*;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CsvImportService {

    @Autowired private BranchRepository branchRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private FacultyAssignmentRepository assignmentRepository;

    public Map<String, Object> importFromCsv(MultipartFile file) throws Exception {
        int imported = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(reader);

        for (CSVRecord record : parser) {
            try {
                String facultyName = record.get("faculty_name");
                String subjectName = record.get("subject_name");
                String branchName  = record.get("branch").toUpperCase();
                int year           = Integer.parseInt(record.get("year"));

                // Auto-detect subject type
                String type = subjectName.toLowerCase().contains("lab") ? "LAB" : "THEORY";
                int hoursPerWeek   = type.equals("LAB") ? 2 : 4;

                // Save branch if not exists
                Branch branch = branchRepository.findByName(branchName)
                        .orElseGet(() -> {
                            Branch b = new Branch();
                            b.setName(branchName);
                            b.setTotalSections(2); // default, can be updated
                            return branchRepository.save(b);
                        });

                // Save faculty if not exists
                facultyRepository.findByName(facultyName)
                        .orElseGet(() -> {
                            Faculty f = new Faculty();
                            f.setName(facultyName);
                            f.setDepartment(branchName);
                            f.setMaxHoursPerWeek(20);
                            return facultyRepository.save(f);
                        });

                // Save subject if not exists
                subjectRepository.findByBranchAndYear(branchName, year)
                        .stream()
                        .filter(s -> s.getName().equalsIgnoreCase(subjectName))
                        .findFirst()
                        .orElseGet(() -> {
                            Subject s = new Subject();
                            s.setName(subjectName);
                            s.setBranch(branchName);
                            s.setYear(year);
                            s.setType(type);
                            s.setHoursPerWeek(hoursPerWeek);
                            return subjectRepository.save(s);
                        });

                // Create assignment for each section of this branch+year
                int totalSections = branch.getTotalSections();
                for (int i = 0; i < totalSections; i++) {
                    String section = String.valueOf((char) ('A' + i));
                    boolean exists = assignmentRepository
                            .findByBranchAndYearAndSection(branchName, year, section)
                            .stream()
                            .anyMatch(a -> a.getFacultyName().equals(facultyName)
                                    && a.getSubjectName().equals(subjectName));
                    if (!exists) {
                        FacultyAssignment fa = new FacultyAssignment();
                        fa.setFacultyName(facultyName);
                        fa.setSubjectName(subjectName);
                        fa.setBranch(branchName);
                        fa.setYear(year);
                        fa.setSection(section);
                        assignmentRepository.save(fa);
                    }
                }
                imported++;
            } catch (Exception e) {
                errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                skipped++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("imported", imported);
        result.put("skipped", skipped);
        result.put("errors", errors);
        return result;
    }
}