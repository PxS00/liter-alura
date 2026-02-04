package br.com.literalura.repository;

import br.com.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Author> authorsByLivingYear(Integer year);

    Optional<Author> findByNameIgnoreCaseAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);
}
