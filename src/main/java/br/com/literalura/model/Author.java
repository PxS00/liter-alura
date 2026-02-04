package br.com.literalura.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name", "birthYear", "deathYear"}
        )
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Book> book;

    public Author(AuthorData authorData) {
        this.name = authorData.name();
        this.birthYear = authorData.birthYear();
        this.deathYear = authorData.deathYear();
    }

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    @Override
    public String toString() {

        String booksTitles = book == null || book.isEmpty()
                ? "No books"
                : book.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));

        return """
            ----- AUTHOR -----
            Name: %s
            Birth Year: %s
            Death Year: %s
            Books: %s
            -----------------
            """.formatted(
                name,
                birthYear != null ? birthYear : "Unknown",
                deathYear != null ? deathYear : "Alive",
                booksTitles
        );
    }
}
