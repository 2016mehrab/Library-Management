package com.eshan.library.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.LibrarianRepository;
import com.eshan.library.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final LibrarianRepository librarianRepository;

    @Bean
    public UserDetailsService studentDetailsService() {
        return username -> studentRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Student not found!"));
    }

    @Bean
    public UserDetailsService adminDetailsService() {
        return username -> adminRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Admin not found!"));
    }

    @Bean
    public UserDetailsService librarianDetailsService() {
        return username -> librarianRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Librarian not found!"));

    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.startsWith("S_")) {
                return studentDetailsService().loadUserByUsername(username);
            } else if (username.startsWith("L_")) {
                return librarianDetailsService().loadUserByUsername(username);
            } else if (username.startsWith("A_")) {
                return adminDetailsService().loadUserByUsername(username);
            } else {
                throw new RuntimeException("AuthenticationProvider failed to find user with that Prefix!");
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
