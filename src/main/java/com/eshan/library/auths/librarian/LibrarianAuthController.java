package com.eshan.library.auths.librarian;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.auths.AuthenticationRequest;
import com.eshan.library.auths.AuthenticationResponse;
import com.eshan.library.services.LibrarianDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/librarian-auth")
@RequiredArgsConstructor
public class LibrarianAuthController {
    private final LibrarianAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody LibrarianDTO request) {
        AuthenticationResponse res = authService.register(request);
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
