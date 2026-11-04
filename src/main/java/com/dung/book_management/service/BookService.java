package com.dung.book_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.exception.BookNotFoundException;
import com.dung.book_management.exception.DuplicateBookException;
import com.dung.book_management.model.Book;
import com.dung.book_management.repository.BookRepository;

@Service
public class BookService {
        private final BookRepository bookRepository;

        public BookService(BookRepository bookRepository) {
                this.bookRepository = bookRepository;
        }

        public List<BookResponse> findAll(String keyword, Integer fromYear, Integer toYear, String author) {
                return bookRepository.search(blankToNull(keyword), blankToNull(author), fromYear, toYear)
                                .stream().map(b -> BookResponse.from(b)).toList();
        }

        public BookResponse findById(Long id) {
                return bookRepository.findById(id)
                                .map(b -> BookResponse.from(b))
                                .orElseThrow(() -> new BookNotFoundException(id));
        }

        public List<BookResponse> findByAuthor(String author) {
                return bookRepository.findByAuthorIgnoreCase(author).stream()
                                .map(b -> BookResponse.from(b))
                                .toList();
        }

        public Long count() {
                return bookRepository.count();
        }

        public BookResponse create(BookRequest request) {
                if (existsByTitleAndAuthor(request.title(), request.author()))
                        throw new DuplicateBookException(request.title(), request.author());
                Book book = new Book(request.title(), request.author(), request.publishedYear());
                Book saved = bookRepository.save(book);
                return BookResponse.from(saved);
        }

        public BookResponse update(Long id, BookRequest request) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new BookNotFoundException(id));
                book.setTitle(request.title());
                book.setAuthor(request.author());
                book.setPublishedYear(request.publishedYear());
                return BookResponse.from(bookRepository.save(book));
        }

        public void delete(Long id) {
                if (!bookRepository.existsById(id)) {
                        throw new BookNotFoundException(id);
                }
                bookRepository.deleteById(id);
        }

        public boolean existsByTitleAndAuthor(String title, String author) {
                return bookRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCase(title, author);
        }

        private String blankToNull(String value) {
                return (value == null || value.isBlank()) ? null : value.trim();
        }
}
