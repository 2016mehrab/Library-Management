package com.eshan.library.auths.student;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshan.library.auths.AuthenticationRequest;
import com.eshan.library.auths.AuthenticationResponse;
import com.eshan.library.auths.RegisterRequest;
import com.eshan.library.configs.JwtService;
import com.eshan.library.models.Role;
import com.eshan.library.models.Student;
import com.eshan.library.repositories.StudentRepository;
import com.eshan.library.services.StudentDTO;
import com.eshan.library.services.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentAuthService {
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(StudentDTO request) {
        // var student = Student.builder()
        //         .username(request.getUsername())
        //         .password(passwordEncoder.encode(request.getPassword()))
        //         .role(Role.STUDENT)
        //         .build();
        var student = studentService.save(request);
        var jwtToken = jwtService.generateToken(student);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));
        var student = studentRepository.findByUsername(request.getUsername());
        if(student.isPresent()){
        var jwtToken = jwtService.generateToken(student.get());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        }
        return null;

    }

}
