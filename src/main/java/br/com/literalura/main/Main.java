package br.com.literalura.main;

import br.com.literalura.model.ApiResponseData;
import br.com.literalura.model.Author;
import br.com.literalura.model.Book;
import br.com.literalura.model.BookData;
import br.com.literalura.repository.AuthorRepository;
import br.com.literalura.repository.BookRepository;
import br.com.literalura.service.ApiClient;
import br.com.literalura.service.AuthorService;
import br.com.literalura.service.BookService;
import br.com.literalura.service.JsonParserImpl;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Scanner;

@Component
public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final BookService bookService;
    private final AuthorService authorService;

    public Main(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    public void showMenu() {
        var op = -1;
        while (op != 0) {
            var menu = """
                        ===== LiterAlura Menu =====
                     1 - Search book
                     2 - Search author
                     3 - Stored books
                     4 - Stored authors 
                     5 - List of living authors as of a given year.
                     6 - List of books in a specific language
                    \s
                     0 - Exit                                \s
                    \s""";

            System.out.println(menu);
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    searchBookWeb();
                    break;
                case 2:
                    searchAuthor();
                    break;
                case 3:
                    storedBooks();
                    break;
                case 4:
                    storedAuthors();
                    break;
                case 5:
                    authorsfromYear();
                    break;
                case 6:
                    booksByLanguage();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void searchBookWeb() {

        System.out.println("Enter the book name to search:");
        var title = sc.nextLine();

        bookService.searchAndSaveBook(title);
    }

    private void searchAuthor() {
        System.out.println("Enter the book name to find its author:");
        var title = sc.nextLine();

        authorService.searchAuthorByBook(title);
    }

    private void storedBooks() {
        bookService.listStoredBooks();
    }

    private void storedAuthors() {
        authorService.listStoredAuthors();
    }

    private void authorsfromYear() {
        System.out.println("Enter a year to find authors alive in that year:");
        var year = sc.nextInt();
        sc.nextLine();
        authorService.listAuthorsAliveInYear(year);
    }

    private void booksByLanguage() {

        System.out.println("""
        Enter a language to perform the search:
        es - Spanish
        en - English
        fr - French
        pt - Portuguese
        """);

        var language = sc.nextLine().trim();

        bookService.listBooksByLanguage(language);
    }
}

