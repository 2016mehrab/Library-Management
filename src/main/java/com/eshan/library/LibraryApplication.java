package com.eshan.library;

import com.eshan.library.models.*;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.AuthorRepository;
import com.eshan.library.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import javax.sql.DataSource;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder, DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return args -> {
			Integer lockResult =  jdbcTemplate.queryForObject("SELECT GET_LOCK('admin_init_lock', 10)", Integer.class);
			if (lockResult!=null && lockResult==1){
				try{

			if (adminRepository.count() == 0 && adminRepository.findByUsername("admin").isEmpty()) {
				Admin admin = Admin.builder().
						username("A_admin").
						password(passwordEncoder.encode("admin")).
						role(Role.ADMIN).
						build();
				adminRepository.save(admin);
				System.out.println("Initial admin created");
			}
				}finally{
					jdbcTemplate.execute("SELECT RELEASE_LOCK('admin_init_lock')");
				}
			}
			else
				System.out.println("Failed to acquire lock for admin initialization");
		};
	}

}
