package com.indhu.college_scheduler.controller;

import com.indhu.college_scheduler.model.Branch;
import com.indhu.college_scheduler.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@CrossOrigin(origins = "*")
public class BranchController {

    @Autowired private BranchRepository branchRepository;

    @GetMapping
    public List<Branch> getAll() {
        return branchRepository.findAll();
    }

    @PostMapping
    public Branch add(@RequestBody Branch branch) {
        branch.setName(branch.getName().toUpperCase());
        return branchRepository.save(branch);
    }

    @PutMapping("/{id}")
    public Branch update(@PathVariable Long id, @RequestBody Branch updated) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setName(updated.getName().toUpperCase());
        branch.setTotalSections(updated.getTotalSections());
        return branchRepository.save(branch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        branchRepository.deleteById(id);
    }
}