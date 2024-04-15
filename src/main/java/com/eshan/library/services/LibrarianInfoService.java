package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Admin;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.LibrarianInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibrarianInfoService {
    private final LibrarianInfoRepository librarianInfoRepository;
    private final AdminRepository adminRepository;

    // public LibrarianInfoService(LibrarianInfoRepository librarianInfoRepository,
    // AdminRepository adminRepository) {
    // this.librarianInfoRepository = librarianInfoRepository;
    // this.adminRepository = adminRepository;
    // }
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

    public LibrarianInfo findLibrarianInfoById(Integer id) {
        return librarianInfoRepository.findById(id).orElse(null);
    }

    public List<LibrarianInfo> findAllLibrarianInfo() {
        return librarianInfoRepository.findAll();
    }

    public void deleteLibrarianInfo(Integer id) {
        librarianInfoRepository.deleteById(id);
    }

    public void updateLibrarianInfoEmail(LibrarianInfo librarianInfo, Integer id) {
        LibrarianInfo updatedLI = librarianInfoRepository.findById(id).orElse(null);
        if (librarianInfo != null) {
            updatedLI.setEmail(librarianInfo.getEmail());
            librarianInfoRepository.save(updatedLI);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

    public void updateLibrarianInfoName(LibrarianInfo librarianInfo, Integer id) {
        LibrarianInfo updatedLI = librarianInfoRepository.findById(id).orElse(null);
        if (librarianInfo != null) {
            updatedLI.setName(librarianInfo.getName());
            librarianInfoRepository.save(updatedLI);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

}
