package com.eshan.library;

import com.eshan.library.models.Admin;
import com.eshan.library.repositories.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AdminRepository adminRepository
//	){
//		return args->{
//			var admin = Admin.builder()
//					.username("eshan")
//					.password("mock")
//					.build();
//			adminRepository.save(admin);
//		};
//
//	}

}
