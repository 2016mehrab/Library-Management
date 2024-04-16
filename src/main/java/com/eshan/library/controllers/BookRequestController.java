package com.eshan.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.BookRequest;
import com.eshan.library.services.BookRequestService;
import com.eshan.library.services.bookRequestDTO.BookRequestDTO;
import com.eshan.library.services.bookRequestDTO.StatusUpdateDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bookrequests")
@AllArgsConstructor
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
    public List<BookRequest> findAll() {

        return bookRequestService.findAll();
    }

    @PutMapping("{Id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BookRequest> updateCoverLink(@RequestBody StatusUpdateDTO dto,
            @PathVariable("Id") Integer id) {
        try {
            bookRequestService.updateStatus(dto, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
