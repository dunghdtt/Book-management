package com.dung.book_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.exception.BookNotFoundException;
import com.dung.book_management.exception.DuplicateBookException;
import com.dung.book_management.model.Book;

@Service
public class BookService {
        private final List<Book> books = new ArrayList<>(List.of(
                        new Book(1L, "Clean code", "Robert C. Martin", 2008),
                        new Book(2L, "Effective java", "Joshua Bloch", 2018),
                        new Book(3L, "Testing platform", "Joshua Bloch", 2018),
                        new Book(4L, "Security Code", "Joshua Bloch", 2018),
                        new Book(5L, "Head First Java", "Katty Sierra", 2005)));
        private long nextId = 6;

        public BookService() {
                System.out.println("BookService duoc tao");
        }

        public List<BookResponse> findAll(String keyword, Integer fromYear, Integer toYear, String author) {
                return books.stream()
                                .filter(b -> keyword == null || keyword.isBlank()
                                                || b.title().toLowerCase().contains(keyword.toLowerCase()))
                                .filter(b -> author == null || author.isBlank()
                                                || b.author().toLowerCase().contains(author.toLowerCase()))
                                .filter(b -> fromYear == null || b.publishedYear() >= fromYear)
                                .filter(b -> toYear == null || b.publishedYear() <= toYear)
                                .map(b -> BookResponse.from(b))
                                .toList();
        }

        public BookResponse findById(Long id) {
                return books.stream()
                                .filter(b -> b.id().equals(id))
                                .findFirst()
                                .map(b -> BookResponse.from(b))
                                .orElseThrow(() -> new BookNotFoundException(id));
        }

        public List<BookResponse> findByAuthor(String author) {
                return books.stream()
                                .filter(b -> b.author().equalsIgnoreCase(author))
                                .map(b -> BookResponse.from(b))
                                .toList();
        }

        public int count() {
                return books.size();
        }

        public BookResponse create(BookRequest request) {
                if (existsByTitleAndAuthor(request.title(), request.author()))
                        throw new DuplicateBookException(request.title(), request.author());
                Book saved = new Book(nextId++, request.title(), request.author(), request.publishedYear());
                books.add(saved);
                return BookResponse.from(saved);
        }

        public BookResponse update(Long id, BookRequest request) {
                for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).id().equals(id)) {
                                Book updated = new Book(id, request.title(), request.author(), request.publishedYear());
                                books.set(i, updated);
                                return BookResponse.from(updated);
                        }
                }
                throw new BookNotFoundException(id);
        }

        public void delete(Long id) {
                boolean removed = books.removeIf(b -> b.id().equals(id));
                if (!removed)
                        throw new BookNotFoundException(id);
        }

        public boolean existsByTitleAndAuthor(String title, String author) {
                return books.stream().anyMatch(
                                b -> b.title().equalsIgnoreCase(title) && b.author().equalsIgnoreCase(author));
        }
}
