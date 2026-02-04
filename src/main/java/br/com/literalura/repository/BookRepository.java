package br.com.literalura.repository;

import br.com.literalura.model.Author;
import br.com.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleContainingIgnoreCase(String bookTitle);

    List<Book> findByLanguageContainingIgnoreCase(String language);
}
