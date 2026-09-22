package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.security.entity.UserInfo;
import com.example.security.repository.UserInfoRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userInfoRepository.findByName("trung").isEmpty()) {
            UserInfo admin = UserInfo.builder()
                    .name("trung")
                    .email("trungnhspkt@gmail.com")
                    .password(passwordEncoder.encode("123"))
                    .roles("ROLE_ADMIN")
                    .build();
            userInfoRepository.save(admin);
        }

        if (userInfoRepository.findByName("user").isEmpty()) {
            UserInfo normalUser = UserInfo.builder()
                    .name("user")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("123"))
                    .roles("ROLE_USER")
                    .build();
            userInfoRepository.save(normalUser);
        }
    }
}
