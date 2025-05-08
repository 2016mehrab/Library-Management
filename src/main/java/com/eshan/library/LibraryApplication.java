package com.eshan.library;

import com.eshan.library.models.*;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.AuthorRepository;
import com.eshan.library.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (adminRepository.count() == 0 && adminRepository.findByUsername("admin").isEmpty()) {
				Admin admin = Admin.builder().
						username("A_admin").
						password(passwordEncoder.encode("admin")).
						role(Role.ADMIN).
						build();
				adminRepository.save(admin);
				System.out.println("Initial admin created");
			}
		};
	}

}
