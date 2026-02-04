package br.com.literalura.service;

import br.com.literalura.model.*;
import br.com.literalura.repository.AuthorRepository;
import br.com.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ApiClient apiClient;
    private final JsonParserImpl jsonParser;
    private final String ADDRESS = "https://gutendex.com/books?search=";

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, ApiClient apiClient, JsonParserImpl jsonParser) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.apiClient = apiClient;
        this.jsonParser = jsonParser;
    }

    public void searchAndSaveBook(String title) {
        ApiResponseData response = fetchBookFromApi(title);

        if (response.results().isEmpty()) {
            System.out.println("No book found.");
            return;
        }

        BookData data = chooseBestResult(response);

        if (bookExists(data)) {
            System.out.println("Book already exists.");
            return;
        }
        Book book = new Book(data);
        List<Author> authors = data.author()
                .stream()
                .map(this::getOrCreateAuthor)
                .collect(Collectors.toList());

        book.setAuthor(authors);
        bookRepository.save(book);
        System.out.println("Book saved successfully.");
        System.out.println(book);
    }

    private ApiResponseData fetchBookFromApi(String title) {
        var json = apiClient.getData(
                ADDRESS + title.replace(" ", "%20")
        );
        return jsonParser.parse(json, ApiResponseData.class);
    }

    private BookData chooseBestResult(ApiResponseData response) {
        return response.results()
                .stream()
                .max(Comparator.comparing(BookData::downloads))
                .orElseThrow();
    }

    private boolean bookExists(BookData data) {
        String language = data.language().isEmpty()
                ? "unknown"
                : data.language().get(0);

        String author = data.author().isEmpty()
                ? "unknown"
                : data.author().get(0).name();

        return bookRepository
                .findByTitleAuthorAndLanguage(data.title(), author, language)
                .isPresent();
    }

    public List<Book> findBookByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public void listStoredBooks() {
        bookRepository.findAll()
                .forEach(System.out::println);
    }

    public void listBooksByLanguage(String language) {
        bookRepository
                .findByLanguageContainingIgnoreCase(language)
                .forEach(System.out::println);
    }

    private Author getOrCreateAuthor(AuthorData data) {

        return authorRepository
                .findByNameIgnoreCaseAndBirthYearAndDeathYear(
                        data.name(),
                        data.birthYear(),
                        data.deathYear()
                )
                .orElseGet(() -> authorRepository.save(new Author(data)));
    }
}
