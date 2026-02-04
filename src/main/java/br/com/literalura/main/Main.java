package br.com.literalura.main;

import br.com.literalura.model.ApiResponseData;
import br.com.literalura.model.Book;
import br.com.literalura.model.BookData;
import br.com.literalura.repository.BookRepository;
import br.com.literalura.service.ApiClient;
import br.com.literalura.service.JsonParserImpl;

import java.util.Scanner;

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final ApiClient apiClient = new ApiClient();
    private final JsonParserImpl jsonParser = new JsonParserImpl();
    private final BookRepository bookRepository;
    private final String ADDRESS = "https://gutendex.com/books/?search=";

    public Main(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void showMenu() {
        var op = -1;
        while (op != 0) {
            var menu = """
                     \s
                     1 - Search book
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
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void searchBookWeb() {
        ApiResponseData responseData = getBookData();
        if (responseData.results().isEmpty()) {
            System.out.println("No book found with the given title.");
            return;
        }
        BookData data = responseData.results().get(0);
        Book book = new Book(data);
        bookRepository.save(book);
        System.out.println("Book saved: " + book);
    }

    private ApiResponseData getBookData() {
        System.out.println("Enter the book title:");
        var bookTitle = sc.nextLine();
        var json = apiClient.getData(
                ADDRESS + bookTitle.replace(" ", "%20")
        );
        return jsonParser.parse(json, ApiResponseData.class);
    }

}
