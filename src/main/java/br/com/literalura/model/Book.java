package br.com.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Author> author = new ArrayList<>();
    private String language;
    private Integer downloads;

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.author = bookData.author().stream()
                .map(Author::new)
                .toList();
        this.author.forEach(a -> a.setBooks(this));
        this.language = bookData.language().isEmpty() ? "Unknown" : bookData.language().get(0);
        this.downloads = bookData.downloads();
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        author.forEach(a -> a.setBooks(this));
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}
