package com.dung.book_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dung.book_management.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
