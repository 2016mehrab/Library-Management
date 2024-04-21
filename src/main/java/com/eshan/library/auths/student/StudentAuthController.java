package com.eshan.library.auths.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.auths.AuthenticationRequest;
import com.eshan.library.auths.AuthenticationResponse;
import com.eshan.library.auths.RegisterRequest;
import com.eshan.library.services.StudentDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student-auth")
@RequiredArgsConstructor
public class StudentAuthController {
    private final StudentAuthService authService;

     @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody StudentDTO request) {
         AuthenticationResponse res= authService.register(request);
        try {
            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));

    }   
}
