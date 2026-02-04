package br.com.literalura.service;

import br.com.literalura.model.Author;
import br.com.literalura.repository.AuthorRepository;
import br.com.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void searchAuthorByBook(String title) {

        var book = bookRepository.findByTitleContainingIgnoreCase(title);

        book.ifPresentOrElse(b -> {
            System.out.println("Author of the book '" + b.getTitle() + "':");
            b.getAuthor().forEach(System.out::println);
        }, () -> System.out.println("No book found."));
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
