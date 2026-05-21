package com.indhu.college_scheduler.controller;

import com.indhu.college_scheduler.service.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "*")
public class CsvController {

    @Autowired private CsvImportService csvImportService;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        try {
            return csvImportService.importFromCsv(file);
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage());
        }
    }
}