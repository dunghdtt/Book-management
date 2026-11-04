package com.dung.book_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dung.book_management.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNationalityIgnoreCase(String nationality);

    boolean existsByNameIgnoreCaseAndNationalityIgnoreCase(String name, String nationality);

    @Query("""
                SELECT a FROM Author a
                WHERE(:keyword IS NULL OR LOWER(a.name) LIKE LOWER (CONCAT('%',:keyword,'%')))
                AND(:nationality IS NULL OR LOWER(a.nationality) LIKE LOWER (CONCAT('%',:nationality,'%')))
                AND(:fromBirthYear IS NULL OR a.birthYear >= :fromBirthYear)
                AND(:toBirthYear IS NULL OR a.birthYear <= :toBirthYear)
            """)
    List<Author> search(@Param("keyword") String keyword,
            @Param("nationality") String nationality,
            @Param("fromBirthYear") Integer fromBirthYear,
            @Param("toBirthYear") Integer toBirthYear);
}
