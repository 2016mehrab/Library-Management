package com.eshan.library;

import com.eshan.library.models.Admin;
import com.eshan.library.models.Author;
import com.eshan.library.models.Book;
import com.eshan.library.models.Category;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
