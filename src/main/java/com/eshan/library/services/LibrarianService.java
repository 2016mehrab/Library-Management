package com.eshan.library.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshan.library.models.Librarian;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.models.Role;
import com.eshan.library.repositories.LibrarianInfoRepository;
import com.eshan.library.repositories.LibrarianRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibrarianService {
    private final LibrarianRepository librarianRepository;
    private final LibrarianInfoRepository librarianInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public Librarian saveLibrarian(LibrarianDTO librarianDTO) {
        var librarian = toLibrarian(librarianDTO);
        return librarianRepository.save(librarian);
    }

    private Librarian toLibrarian(LibrarianDTO librarianDTO) {
        var librarian = new Librarian();

        librarian.setUsername("L_" + librarianDTO.username());
        librarian.setPassword(passwordEncoder.encode(librarianDTO.password()));
        librarian.setRole(Role.LIBRARIAN);
        librarian.setEmail(librarianDTO.email());

        LibrarianInfo librarianInfo = librarianInfoRepository.findById(librarianDTO.librarianId()).orElse(null);
        if (librarianInfo != null) {
            librarian.setLibrarianInfo(librarianInfo);
            return librarian;
        } else {
            throw new RuntimeException("Operation was not successful: LibrarianInfo not found");

        }
    }

    // TODO: create a response dto
    public Librarian findLibrarianById(Integer id) {
        // LibrarianInfo librarianInfo =
        // librarianInfoRepository.findById(id).orElse(null);
        Librarian librarian = librarianRepository.findById(id).orElse(null);
        return librarian;
    }

    private LibrarianResponseDTO toLibrarianResponseDTO(Librarian lib) {
        return new LibrarianResponseDTO(lib.getProfileId());
    }

    public List<LibrarianResponseDTO> findAllLibrarian() {
        return librarianRepository.findAll().stream().map(this::toLibrarianResponseDTO).collect(Collectors.toList());
    }

    public void deleteLibrarian(Integer id) {
        // LibrarianInfo librarianInfo = librarianInfoRepository.findById(id).orElse(null);
        librarianRepository.deleteById(id);
    }

    // TODO: check this
    public void updateLibrarianPassword(Librarian librarian, Integer id) {
        // LibrarianInfo librarianInfo = librarianInfoRepository.findById(id).orElse(null);
        Librarian updatedL = librarianRepository.findById(id).orElse(null);
        if (librarian != null) {
            updatedL.setPassword(librarian.getPassword());
            librarianRepository.save(updatedL);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

}
