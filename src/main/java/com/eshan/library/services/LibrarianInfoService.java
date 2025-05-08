package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Admin;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.LibrarianInfoRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LibrarianInfoService {

    private final LibrarianInfoRepository librarianInfoRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public LibrarianInfo saveLibrarianInfo(LibrarianInfo librarianInfo) {
        Admin admin = adminRepository.findById(librarianInfo.getAdmin().getId()).orElse(null);
        if (librarianInfoRepository.existsById(librarianInfo.getId())) {
            throw new RuntimeException("Operation was not successful: Librarian exists");
        }
        if (admin != null) {
            librarianInfo.setAdmin(admin);
            return librarianInfoRepository.save(librarianInfo);
        } else {
            throw new RuntimeException("Operation was not successful: Admin not found");
        }
    }

    @Transactional(readOnly = true)
    public LibrarianInfo findLibrarianInfoById(Integer id) {
        return librarianInfoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<LibrarianInfo> findAllLibrarianInfo() {
        return librarianInfoRepository.findAll();
    }

    @Transactional
    public void deleteLibrarianInfo(Integer id) {
        librarianInfoRepository.deleteById(id);
    }

    @Transactional
    public void updateLibrarianInfoEmail(EmailUpdateDTO emailUpdateDTO, Integer id) {
        LibrarianInfo updatedLI = librarianInfoRepository.findById(id).orElse(null);
        if (emailUpdateDTO.email() != null && updatedLI != null) {
            updatedLI.setEmail(emailUpdateDTO.email());
            librarianInfoRepository.save(updatedLI);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

    @Transactional
    public void updateLibrarianInfoName(NameUpdateDTO nameUpdateDTO, Integer id) {
        LibrarianInfo updatedLI = librarianInfoRepository.findById(id).orElse(null);
        if (nameUpdateDTO.name() != null && updatedLI != null) {
            updatedLI.setName(nameUpdateDTO.name());
            librarianInfoRepository.save(updatedLI);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

}
