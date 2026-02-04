package br.com.literalura.service;

import br.com.literalura.model.ApiResponseData;
import br.com.literalura.model.Book;
import br.com.literalura.model.BookData;
import br.com.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ApiClient apiClient;
    private final JsonParserImpl jsonParser;
    private final String ADDRESS = "https://gutendex.com/books?search=";

    public BookService(BookRepository bookRepository, ApiClient apiClient, JsonParserImpl jsonParser) {
        this.bookRepository = bookRepository;
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
        bookRepository.save(new Book(data));
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

    public Optional<Book> findBookByTitle(String title) {
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
}
