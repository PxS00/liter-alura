package br.com.literalura.repository;

import br.com.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
    SELECT b FROM Book b JOIN b.author a
    WHERE LOWER(b.title) = LOWER(:title)
      AND LOWER(a.name) = LOWER(:author)
      AND LOWER(b.language) = LOWER(:language)
""")
    Optional<Book> findByTitleAuthorAndLanguage(
            String title,
            String author,
            String language
    );

    List<Book> findByLanguageContainingIgnoreCase(String language);

    List<Book> findByTitleContainingIgnoreCase(String title);
}
