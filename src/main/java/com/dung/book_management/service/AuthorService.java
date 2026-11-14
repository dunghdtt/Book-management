package com.dung.book_management.service;

import com.dung.book_management.repository.BookRepository;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dung.book_management.dto.request.AuthorRequest;
import com.dung.book_management.dto.response.AuthorResponse;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.exception.AuthorHasBooksException;
import com.dung.book_management.exception.AuthorNotFoundException;
import com.dung.book_management.exception.DuplicateAuthorException;
import com.dung.book_management.mapper.AuthorMapper;
import com.dung.book_management.mapper.BookMapper;
import com.dung.book_management.model.Author;
import com.dung.book_management.repository.AuthorRepository;

@Service
public class AuthorService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public long count() {
        return authorRepository.count();
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll(String keyword, String nationality, Integer fromBirthYear,
            Integer toBirthYear) {
        return authorRepository.search(blankToNull(keyword), blankToNull(nationality), fromBirthYear, toBirthYear)
                .stream().map(AuthorMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorMapper::toResponse)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality).stream()
                .map(AuthorMapper::toResponse)
                .toList();
    }

    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        if (existsByNameAndNationality(request.name(), request.nationality()))
            throw new DuplicateAuthorException(request.name(), request.nationality());
        Author saved = authorRepository.save(AuthorMapper.toEntity(request));
        return AuthorMapper.toResponse(saved);
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        AuthorMapper.updateEntity(author, request);
        return AuthorMapper.toResponse(author);
    }

    @Transactional
    public void delete(Long id) {
        if (!authorRepository.existsById(id))
            throw new AuthorNotFoundException(id);
        if (bookRepository.existsByAuthorId(id))
            throw new AuthorHasBooksException(id);
        authorRepository.deleteById(id);
    }

    public boolean existsByNameAndNationality(String name, String nationality) {
        return authorRepository.existsByNameIgnoreCaseAndNationalityIgnoreCase(name, nationality);
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findBooksOfAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        return bookRepository.findByAuthorId(id).stream().map(BookMapper::toResponse).toList();
    }
}
