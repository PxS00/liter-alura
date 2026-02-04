package br.com.literalura.repository;

import br.com.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String bookTitle);

    List<Book> findByLanguageContainingIgnoreCase(String language);
}
