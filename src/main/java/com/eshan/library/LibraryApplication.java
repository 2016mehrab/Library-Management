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
	@Bean
	public CommandLineRunner commandLineRunner(
			AdminRepository adminRepository,
			BookRepository bookRepository
	){
		return args -> {
			// Create some sample categories
			Category horrorCategory = new Category();
			horrorCategory.setName("Horror");

			Category goreCategory = new Category();
			goreCategory.setName("Gore");

			// Create some sample authors
			Author mithulAuthor = new Author();
			mithulAuthor.setName("Mithul");

			Author brenAuthor = new Author();
			brenAuthor.setName("Bren");

			// Create the book object and add categories and authors
			var book = Book.builder()
					.id(13)
					.title("Manush")
					.price(23.39)
					.categories(Arrays.asList(horrorCategory, goreCategory))
					.quantity(2)
					.authors(Arrays.asList(mithulAuthor, brenAuthor))
					.build();

			// Save the book
			bookRepository.save(book);
		};

	}

}
