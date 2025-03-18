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

import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.services.LibrarianInfoService;

@RestController
@RequestMapping("/librarianinfo")
@CrossOrigin
public class LibrarianInfoController {
    private final LibrarianInfoService librarianInfoService;

    public LibrarianInfoController(LibrarianInfoService librarianInfoService) {
        this.librarianInfoService = librarianInfoService;
    }

    @PostMapping
    public ResponseEntity<LibrarianInfo> post(@RequestBody LibrarianInfo librarianInfo) {
        try {
            LibrarianInfo savedLI = librarianInfoService.saveLibrarianInfo(librarianInfo);
            return new ResponseEntity<LibrarianInfo>(savedLI, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{libId}")
    public LibrarianInfo findById(@PathVariable("libId") Integer id) {
        return librarianInfoService.findLibrarianInfoById(id);
    }

    @GetMapping
    public List<LibrarianInfo> findAllLibrarianInfo() {
        return librarianInfoService.findAllLibrarianInfo();
    }

    @DeleteMapping("{libId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudentInfo(@PathVariable("libId") Integer id) {
        librarianInfoService.deleteLibrarianInfo(id);
    }

    @PutMapping("email/{libId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LibrarianInfo> updateEmail(@RequestBody LibrarianInfo librarianInfo,
            @PathVariable("libId") Integer id) {
        try {
            librarianInfoService.updateLibrarianInfoEmail(librarianInfo, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("name/{libId}")
    public ResponseEntity<LibrarianInfo> updateName(@RequestBody LibrarianInfo librarianInfo,
            @PathVariable("libId") Integer id) {
        try {
            librarianInfoService.updateLibrarianInfoName(librarianInfo, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
