package br.com.literalura.service;

import br.com.literalura.model.Author;
import br.com.literalura.model.Book;
import br.com.literalura.repository.AuthorRepository;
import br.com.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Scanner;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void searchAuthorByBook(String title, Scanner sc) {

        var books = bookRepository.findByTitleContainingIgnoreCase(title);
        if (books.isEmpty()) {
            System.out.println("No book found.");
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);

            System.out.printf(
                    "%d - %s (%s)%n",
                    i + 1,
                    b.getTitle(),
                    b.getLanguage()
            );
        }
        System.out.println("\nChoose a book by number:");

        int choice = sc.nextInt();
        sc.nextLine();
        if (choice < 1 || choice > books.size()) {
            System.out.println("Invalid option.");
            return;
        }
        Book selectedBook = books.get(choice - 1);
        System.out.println("\nAuthors of '" + selectedBook.getTitle() + "':");
        selectedBook.getAuthor().forEach(System.out::println);
    }

    public void listStoredAuthors() {
        System.out.println("\nStored authors in the database:\n");

        authorRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Author::getId))
                .forEach(System.out::println);
    }

    public void listAuthorsAliveInYear(int year) {
        var authors = authorRepository.authorsByLivingYear(year);

        if (authors.isEmpty()) {
            System.out.println("No authors found in year " + year);
            return;
        }

        System.out.println("Authors alive in " + year + ":");
        authors.forEach(System.out::println);
    }


}
