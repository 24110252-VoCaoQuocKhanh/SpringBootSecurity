package com.example.security.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.security.entity.UserInfo;
import com.example.security.repository.UserInfoRepository;

@Service
public class UserService {

    private final UserInfoRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserInfoRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        Optional<UserInfo> existing = repository.findByName(userInfo.getName());
        if (existing.isPresent()) {
            UserInfo u = existing.get();
            u.setEmail(userInfo.getEmail());
            u.setPassword(userInfo.getPassword());
            u.setRoles(userInfo.getRoles());
            repository.save(u);
        } else {
            repository.save(userInfo);
        }
        return "Thêm user thành công!";
    }
}
