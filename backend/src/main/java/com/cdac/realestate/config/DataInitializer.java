package com.cdac.realestate.config;

import com.cdac.realestate.entity.User;
import com.cdac.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    com.cdac.realestate.repository.PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Admin
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@test.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setPhone("9999999999");
            admin.setAddress("Admin HQ");
            admin.setRole(User.Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Seeded: admin@test.com");
        }
    }
}
