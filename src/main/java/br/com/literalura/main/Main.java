package br.com.literalura.main;

import br.com.literalura.model.ApiResponseData;
import br.com.literalura.model.Book;
import br.com.literalura.model.BookData;
import br.com.literalura.repository.AuthorRepository;
import br.com.literalura.repository.BookRepository;
import br.com.literalura.service.ApiClient;
import br.com.literalura.service.JsonParserImpl;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Scanner;

@Component
public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final ApiClient apiClient = new ApiClient();
    private final JsonParserImpl jsonParser = new JsonParserImpl();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final String ADDRESS = "https://gutendex.com/books?search=";

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        var op = -1;
        while (op != 0) {
            var menu = """
                     \s
                     1 - Search book
                     2 - Search author
                     3 - List stored books
                     4 - List of living authors as of a given year.
                     5 - List of books in a specific language
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
                    authorsfromYear();
                    break;
                case 5:
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
        var bookTitle = sc.nextLine();

        if (bookRepository.findByTitleIgnoreCase(bookTitle).isPresent()) {
            System.out.println("Book already exists in the database.");
            return;
        }

            ApiResponseData responseData = getBookData(bookTitle);
        if (responseData.results().isEmpty()) {
            System.out.println("No book found with the given title.");
            return;
        }
        BookData data = responseData.results().get(0);
        Book book = new Book(data);
        bookRepository.save(book);
        System.out.println("Book saved: " + book);
    }

    private ApiResponseData getBookData(String title) {
        var json = apiClient.getData(
                ADDRESS + title.replace(" ", "%20")
        );
        return jsonParser.parse(json, ApiResponseData.class);
    }

    private void searchAuthor() {
        storedBooks();
        System.out.println("Enter the book name to search for its author:");
        var bookTitle = sc.nextLine();
        var bookOpt = bookRepository.findByTitleIgnoreCase(bookTitle);

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            System.out.println("Authors of the book '" + book.getTitle() + "':");
            book.getAuthor().forEach(System.out::println);
        } else {
            System.out.println("No book found with the given title.");
        }
    }

    private void storedBooks() {
        var books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getId))
                .forEach(b -> System.out.println(b.getTitle()));
    }

    private void authorsfromYear() {
        System.out.println("Enter the year to find living authors as of that year:");
        var year = sc.nextInt();
        sc.nextLine();
        var authors = authorRepository.authorsByLivingYear(year);
        if (authors.isEmpty()) {
            System.out.println("No authors found who were alive in the year " + year + ".");
        } else {
            System.out.println("Authors who were alive in the year " + year + ":");
            authors.forEach(System.out::println);
        }
    }

    private void booksByLanguage() {
        System.out.println("Enter the language to find books:");
        var language = sc.nextLine();
        var books = bookRepository.findByLanguageContainingIgnoreCase(language);
        if (books.isEmpty()) {
            System.out.println("No books found in the language: " + language + ".");
        } else {
            System.out.println("Books in the language '" + language + "':");
            books.forEach(b -> System.out.println(b.getTitle()));
        }
    }
}

