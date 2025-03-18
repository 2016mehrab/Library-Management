package com.eshan.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.Librarian;
import com.eshan.library.services.LibrarianDTO;
import com.eshan.library.services.LibrarianResponseDTO;
import com.eshan.library.services.LibrarianService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/librarians")
@AllArgsConstructor
@CrossOrigin
public class LibrarianController {
    private final LibrarianService librarianService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody LibrarianDTO librarianDTO) {
        try {
            Librarian savedL = librarianService.saveLibrarian(librarianDTO);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{libId}")
    public Librarian LibrarianfindById(@PathVariable("libId") Integer id) {
        return librarianService.findLibrarianById(id);
    }

    @GetMapping
    public List<LibrarianResponseDTO> findAllLibrarians() {
        return librarianService.findAllLibrarian();
    }

    @DeleteMapping("{libId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLibrarian(@PathVariable("libId") Integer id) {
        librarianService.deleteLibrarian(id);
    }

    @PutMapping("{libId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Librarian> updatePassword(@RequestBody Librarian librarian,
            @PathVariable("libId") Integer id) {
        try {
            librarianService.updateLibrarianPassword(librarian, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
