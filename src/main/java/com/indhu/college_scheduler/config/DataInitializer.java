package com.indhu.college_scheduler.config;

import com.indhu.college_scheduler.model.User;
import com.indhu.college_scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default users if they don't exist
        if (userRepository.count() == 0) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setFullName("System Administrator");
            admin.setDepartment("IT");
            userRepository.save(admin);

            User principal = new User();
            principal.setUsername("principal");
            principal.setPassword(passwordEncoder.encode("principal123"));
            principal.setRole("PRINCIPAL");
            principal.setFullName("Dr. College Principal");
            principal.setDepartment("Administration");
            userRepository.save(principal);

            User faculty = new User();
            faculty.setUsername("faculty");
            faculty.setPassword(passwordEncoder.encode("faculty123"));
            faculty.setRole("FACULTY");
            faculty.setFullName("Dr. Sample Faculty");
            faculty.setDepartment("IT");
            userRepository.save(faculty);

            System.out.println("✅ Default users created:");
            System.out.println("   admin / admin123");
            System.out.println("   principal / principal123");
            System.out.println("   faculty / faculty123");
        }
    }
}