package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Librarian;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.repositories.LibrarianInfoRepository;
import com.eshan.library.repositories.LibrarianRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LibrarianService {
    private final LibrarianRepository librarianRepository;
    private final LibrarianInfoRepository librarianInfoRepository;

    public Librarian saveLibrarian(Librarian librarian) {
        LibrarianInfo librarianInfo = librarianInfoRepository.findById(librarian.getId()).orElse(null);
        if (librarianRepository.existsById(librarian.getId())) {
            throw new RuntimeException("Operation was not successful: Librarian exists!");
        }
        if (librarianInfo != null) {
            librarian.setLibrarianInfo(librarianInfo);
            return librarianRepository.save(librarian);
        } else {
            throw new RuntimeException("Operation was not successful: Admin not found");
        }
    }

    public Librarian findLibrarianById(Integer id){
        return librarianRepository.findById(id).orElse(null);
    }

    public List<Librarian> findAllLibrarian(){
        return librarianRepository.findAll();
    }

    public void deleteLibrarian(Integer id){
        librarianRepository.deleteById(id);
    }


    public void updateLibrarianPassword(Librarian librarian, Integer id){
        Librarian updatedL= librarianRepository.findById(id).orElse(null);
        if(librarian!=null){
            updatedL.setPassword(librarian.getPassword());
            librarianRepository.save(updatedL);
        }else {
            throw new RuntimeException("Operation was not successful: Librarian Not Found!");
        }
    }

}
