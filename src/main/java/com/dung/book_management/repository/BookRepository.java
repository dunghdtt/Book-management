package com.dung.book_management.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dung.book_management.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorNameIgnoreCase(String author);

    boolean existsByTitleIgnoreCaseAndAuthorId(String title, Long author);

    @Query("""
                SELECT b FROM Book b
                WHERE(:keyword IS NULL OR LOWER(b.title) LIKE LOWER (CONCAT('%',:keyword,'%')))
                AND(:author IS NULL OR LOWER(b.author.name) LIKE LOWER (CONCAT('%',:author,'%')))
                AND(:fromYear IS NULL OR b.publishedYear >= :fromYear)
                AND(:toYear IS NULL OR b.publishedYear <= :toYear)
            """)
    Page<Book> search(@Param("keyword") String keyword,
            @Param("author") String author,
            @Param("fromYear") Integer fromYear,
            @Param("toYear") Integer toYear,
            Pageable pageable);

}
