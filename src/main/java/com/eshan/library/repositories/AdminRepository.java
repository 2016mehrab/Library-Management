package com.eshan.library.repositories;

import com.eshan.library.models.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Optional<Admin> findByUsername(String username);
}
