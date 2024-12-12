package com.points.points.Configuration;

import com.points.points.Entity.User;
import com.points.points.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<User> userInfo = userRepository.findByEmail(username);
        System.out.println(userInfo);
        UserDetails s= userInfo.map(e -> new UserInfoUserDetails(e))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username ));
        System.out.println(s);
        return s;
    }
}
