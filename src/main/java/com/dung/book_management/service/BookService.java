package com.dung.book_management.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import com.dung.book_management.dto.PageResponse;
import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.exception.AuthorNotFoundException;
import com.dung.book_management.exception.BookNotFoundException;
import com.dung.book_management.exception.DuplicateBookException;
import com.dung.book_management.exception.InvalidPageException;
import com.dung.book_management.mapper.BookMapper;
import com.dung.book_management.model.Author;
import com.dung.book_management.model.Book;
import com.dung.book_management.repository.AuthorRepository;
import com.dung.book_management.repository.BookRepository;

@Service
public class BookService {
        private final BookRepository bookRepository;
        private final AuthorRepository authorRepository;

        public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
                this.bookRepository = bookRepository;
                this.authorRepository = authorRepository;
        }

        private Pageable createPageable(int page, int size, String sortBy, String direction) {
                if (page < 0) {
                        throw new InvalidPageException("page phai lon hon hoac bang 0");
                }
                if (size < 1) {
                        throw new InvalidPageException("size phai lon hon hoac bang 1");
                }
                Set<String> allowed = Set.of("id", "title", "author.name", "publishedYear");
                if (!allowed.contains(sortBy)) {
                        throw new InvalidPageException("sort by khong hop le: " + sortBy);
                }
                Sort sort = ("asc".equalsIgnoreCase(direction)) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                return PageRequest.of(page, Math.min(size, 50), sort);
        }

        @Transactional(readOnly = true)
        public PageResponse<BookResponse> findAll(String keyword, Integer fromYear, Integer toYear,
                        String author, int page, int size, String sortBy, String direction) {
                Pageable pageable = createPageable(page, size, sortBy, direction);
                Page<Book> bookPage = bookRepository.search(
                                blankToNull(keyword), blankToNull(author), fromYear, toYear, pageable);
                return PageResponse.from(bookPage.map(BookMapper::toResponse));
        }

        @Transactional(readOnly = true)
        public BookResponse findById(Long id) {
                return bookRepository.findById(id)
                                .map(BookMapper::toResponse)
                                .orElseThrow(() -> new BookNotFoundException(id));
        }

        @Transactional(readOnly = true)
        public List<BookResponse> findByAuthor(String author) {
                return bookRepository.findByAuthorNameIgnoreCase(author).stream()
                                .map(BookMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public Long count() {
                return bookRepository.count();
        }

        @Transactional
        public BookResponse create(BookRequest request) {
                Author author = findAuthor(request.authorId());
                if (existsByTitleAndAuthor(request.title(), author.getId()))
                        throw new DuplicateBookException(request.title(), author.getName());
                Book saved = bookRepository.save(BookMapper.toEntity(request, author));
                return BookMapper.toResponse(saved);
        }

        @Transactional
        public BookResponse update(Long id, BookRequest request) {
                Book book = bookRepository.findById(id)
                                .orElseThrow(() -> new BookNotFoundException(id));
                Author author = findAuthor(request.authorId());
                BookMapper.updateEntity(book, request, author);
                return BookMapper.toResponse(book);
        }

        @Transactional
        public void delete(Long id) {
                if (!bookRepository.existsById(id)) {
                        throw new BookNotFoundException(id);
                }
                bookRepository.deleteById(id);
        }

        public boolean existsByTitleAndAuthor(String title, Long authorId) {
                return bookRepository.existsByTitleIgnoreCaseAndAuthorId(title, authorId);
        }

        private String blankToNull(String value) {
                return (value == null || value.isBlank()) ? null : value.trim();
        }

        private Author findAuthor(Long authorId) {
                return authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
        }

        @Transactional(readOnly = true)
        public PageResponse<BookResponse> getBooksPaged(int page, int size) {
                Pageable pageable = createPageable(page, size, "id", "asc");
                Page<Book> bookPage = bookRepository.findAll(pageable);
                return PageResponse.from(bookPage.map(BookMapper::toResponse));
        }
}
