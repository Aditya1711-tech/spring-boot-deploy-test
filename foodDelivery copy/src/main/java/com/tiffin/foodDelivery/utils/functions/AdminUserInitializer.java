package com.tiffin.foodDelivery.utils.functions;

import com.tiffin.foodDelivery.entities.User;
import com.tiffin.foodDelivery.repositories.UserRepository;
import com.tiffin.foodDelivery.security.utils.UserRole;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminUserInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            // Check if admin user exists
            String adminEmail = "admin@example.com";
            if (!userRepository.findByEmail(adminEmail).isPresent()) {
                // Create a new admin user
                User adminUser = new User();
                adminUser.setName("Admin");
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode("admin123")); // Encode the password
                adminUser.setRoles(UserRole.ADMIN);
                adminUser.setAccountEnabled(true);

                userRepository.save(adminUser);
                logger.info("Admin user created with email: {}", adminEmail);
            } else {
                logger.info("Admin user already exists.");
            }
        };
    }
}

