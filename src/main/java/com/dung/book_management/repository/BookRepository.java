package com.dung.book_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dung.book_management.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
