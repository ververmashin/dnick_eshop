package com.example.eshop.data;

import com.example.eshop.entity.UserEntity;
import com.example.eshop.entity.UserRole;
import com.example.eshop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializr {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DataInitializr(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void addData() {
        UserEntity user = new UserEntity("admin","admin","0","admin","admin@gmail.com",passwordEncoder.encode("admin"), UserRole.ADMIN);
        userRepository.save(user);
    }
}
