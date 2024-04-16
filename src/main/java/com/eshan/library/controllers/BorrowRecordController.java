package com.eshan.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.BorrowRecord;
import com.eshan.library.services.BorrowRecordService;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bookrecords")
@AllArgsConstructor
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody BorrowRecordDTO dto) {
        try {
            borrowRecordService.save(dto);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<BorrowRecord> findAll() {

        return borrowRecordService.findAll();
    }

    @DeleteMapping("{Id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("Id") Integer id) {
        borrowRecordService.delete(id);
    }

    @PutMapping("{Id}/return")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BorrowRecord> bookReturn(@PathVariable("Id") Integer id) {
        try {
            borrowRecordService.returnBook(id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{Id}/booklost")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BorrowRecord> lost(
            @PathVariable("Id") Integer id) {
        try {
            borrowRecordService.bookLost(id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{Id}/fine")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Double> calculatefine(
            @PathVariable("Id") Integer id) {
        try {
            return new ResponseEntity<>(borrowRecordService.updateFine(id), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
