package com.points.points.Service;

import com.points.points.Entity.User;
import com.points.points.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public String registerUser(User user) {
        String updatedPassword = passwordEncoder.encode(user.getPassword());
        User updatedUser = new User(user.getName(), user.getEmail(), updatedPassword, user.getRole());
        userRepository.save(updatedUser);
        return "User saved Successfully!";
    }
}
