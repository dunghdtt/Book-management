package com.dung.book_management.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import com.dung.book_management.dto.PageResponse;
import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.exception.BookNotFoundException;
import com.dung.book_management.exception.DuplicateBookException;
import com.dung.book_management.exception.InvalidPageException;
import com.dung.book_management.mapper.BookMapper;
import com.dung.book_management.model.Book;
import com.dung.book_management.repository.BookRepository;

@Service
public class BookService {
        private final BookRepository bookRepository;

        public BookService(BookRepository bookRepository) {
                this.bookRepository = bookRepository;
        }

        private Pageable createPageable(int page, int size, String sortBy, String direction) {
                if (page < 0) {
                        throw new InvalidPageException("page phai lon hon hoac bang 0");
                }
                if (size < 1) {
                        throw new InvalidPageException("size phai lon hon hoac bang 1");
                }
                Set<String> allowed = Set.of("id", "title", "author", "publishedYear");
                if (!allowed.contains(sortBy)) {
                        throw new InvalidPageException("sort by khong hop le: " + sortBy);
                }
                Sort sort = ("asc".equalsIgnoreCase(direction)) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                return PageRequest.of(page, Math.min(size, 50), sort);
        }

        public PageResponse<BookResponse> findAll(String keyword, Integer fromYear, Integer toYear,
                        String author, int page, int size, String sortBy, String direction) {
                Pageable pageable = createPageable(page, size, sortBy, direction);
                Page<Book> bookPage = bookRepository.search(
                                blankToNull(keyword), blankToNull(author), fromYear, toYear, pageable);
                return PageResponse.from(bookPage.map(BookMapper::toResponse));
        }

        public BookResponse findById(Long id) {
                return bookRepository.findById(id)
                                .map(BookMapper::toResponse)
                                .orElseThrow(() -> new BookNotFoundException(id));
        }

        public List<BookResponse> findByAuthor(String author) {
                return bookRepository.findByAuthorIgnoreCase(author).stream()
                                .map(BookMapper::toResponse)
                                .toList();
        }

        public Long count() {
                return bookRepository.count();
        }

        public BookResponse create(BookRequest request) {
                if (existsByTitleAndAuthor(request.title(), request.author()))
                        throw new DuplicateBookException(request.title(), request.author());
                Book saved = bookRepository.save(BookMapper.toEntity(request));
                return BookMapper.toResponse(saved);
        }

        public BookResponse update(Long id, BookRequest request) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new BookNotFoundException(id));
                BookMapper.updateEntity(book, request);
                return BookMapper.toResponse(book);
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

        public PageResponse<BookResponse> getBooksPaged(int page, int size) {
                Pageable pageable = createPageable(page, size, "id", "asc");
                Page<Book> bookPage = bookRepository.findAll(pageable);
                return PageResponse.from(bookPage.map(BookMapper::toResponse));
        }
}
