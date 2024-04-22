package com.eshan.library.auths;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshan.library.configs.JwtService;
import com.eshan.library.models.Admin;
import com.eshan.library.models.Role;
import com.eshan.library.models.Student;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final AdminRepository adminRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var admin = Admin.builder()
                                .username("A_" + request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.ADMIN)
                                .build();
                adminRepository.save(admin);
                var jwtToken = jwtService.generateToken(admin);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {

                try {
                        authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(
                                                        addAdminPrefix(request.getUsername()),
                                                        request.getPassword()));
                } catch (AuthenticationException e) {
                        throw new RuntimeException("Authentication failed: " + e.getMessage());
                }
                var admin = adminRepository.findByUsername(addAdminPrefix(request.getUsername())).orElseThrow();
                var jwtToken = jwtService.generateToken(admin);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        private String addAdminPrefix(String username) {
                return "A_" + username; // Add "A_" prefix
        }

}
