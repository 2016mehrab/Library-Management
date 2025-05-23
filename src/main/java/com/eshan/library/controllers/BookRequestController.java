package com.eshan.library.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.BookRequest;
import com.eshan.library.services.BookRequestService;
import com.eshan.library.services.bookRequestDTO.BookRequestDTO;
import com.eshan.library.services.bookRequestDTO.BookRequestResponseDTO;
import com.eshan.library.services.bookRequestDTO.StatusUpdateDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bookrequests")
@AllArgsConstructor
@CrossOrigin
public class BookRequestController {
    private final BookRequestService bookRequestService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody BookRequestDTO dto) {
        try {
            bookRequestService.save(dto);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public Page<BookRequestResponseDTO> findAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        return bookRequestService.findAll(pageNumber, pageSize);
    }

    @GetMapping("{Id}")
    public ResponseEntity<BookRequestResponseDTO> findbyId(@PathVariable("Id") Integer id) {

        try {

            return new ResponseEntity<>(bookRequestService.findById(id), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{Id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BookRequest> updateStatus(@RequestBody StatusUpdateDTO dto,
            @PathVariable("Id") Integer id) {
        try {
            bookRequestService.updateStatus(dto, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deleteBook(@PathVariable("Id") Integer id) {
        boolean deleted = bookRequestService.delete(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book-request with id " + id + " not found.");
        }
        return ResponseEntity.ok("Book-request with id" + id + " deleted successfully.");
    }

    @GetMapping("/librarian/{librarianId}")
    public ResponseEntity<Page<BookRequestResponseDTO>> getRequestsByLibrarian(@PathVariable Integer librarianId,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        try {
            var requests = bookRequestService.getRequestsForLibrarian(librarianId, pageNumber,
                    pageSize);
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<BookRequestResponseDTO>> getRequestsByStudent(@PathVariable Integer studentId,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        try {
            var requests = bookRequestService.getRequestsForStudent(studentId, pageNumber,
                    pageSize);
            return new ResponseEntity<>(requests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
