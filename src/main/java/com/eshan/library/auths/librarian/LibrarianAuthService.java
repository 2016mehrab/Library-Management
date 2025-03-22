package com.eshan.library.auths.librarian;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.eshan.library.auths.AuthenticationRequest;
import com.eshan.library.auths.AuthenticationResponse;
import com.eshan.library.configs.JwtService;
import com.eshan.library.repositories.LibrarianRepository;
import com.eshan.library.services.LibrarianDTO;
import com.eshan.library.services.LibrarianService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibrarianAuthService {
    private final LibrarianService librarianService;
    private final JwtService jwtService;
    private final LibrarianRepository librarianRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(LibrarianDTO request) {
        var librarian = librarianService.saveLibrarian(request);
        var jwtToken = jwtService.generateTokenWithId(librarian, librarian.getLibrarianInfo().getId());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(addLibrarianPrefix(request.getUsername()),
                        request.getPassword()));
        var librarian = librarianRepository.findByUsername(addLibrarianPrefix(request.getUsername()));
        if (librarian.isPresent()) {
        var jwtToken = jwtService.generateTokenWithId(librarian.get(), librarian.get().getLibrarianInfo().getId());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        return null;
    }

    private String addLibrarianPrefix(String username) {
        return "L_" + username;
    }

}
