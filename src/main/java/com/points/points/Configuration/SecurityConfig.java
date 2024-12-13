package com.points.points.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                .csrf(csrf -> csrf.disable()) // Disables CSRF (customize as per your needs)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users").permitAll() // Open access
                        .requestMatchers("/api/v1/products/Welcome").permitAll() // Open access
                        .anyRequest().authenticated() // All other requests require authentication
                )
                 .formLogin(Customizer.withDefaults())
                 .httpBasic(Customizer.withDefaults());

         return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() { //Authenticates
        return new UserInfoDetailsService(); //Creating this custom class to fetch user details from our db and then authenticate
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationProvider authenticationProvider() { // We need to tell authentication provider that which all service we are using, means beans we have altered
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


}
