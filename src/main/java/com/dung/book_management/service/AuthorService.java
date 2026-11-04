package com.dung.book_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dung.book_management.dto.request.AuthorRequest;
import com.dung.book_management.dto.response.AuthorResponse;
import com.dung.book_management.exception.AuthorNotFoundException;
import com.dung.book_management.exception.DuplicateAuthorException;
import com.dung.book_management.model.Author;
import com.dung.book_management.repository.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public long count() {
        return authorRepository.count();
    }

    public List<AuthorResponse> findAll(String keyword, String nationality, Integer fromBirthYear,
            Integer toBirthYear) {
        return authorRepository.search(blankToNull(keyword), blankToNull(nationality), fromBirthYear, toBirthYear)
                .stream().map(a -> AuthorResponse.from(a)).toList();
    }

    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
                .map(a -> AuthorResponse.from(a))
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<AuthorResponse> findByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality).stream()
                .map(a -> AuthorResponse.from(a))
                .toList();
    }

    public AuthorResponse create(AuthorRequest request) {
        if (existsByNameAndNationality(request.name(), request.nationality()))
            throw new DuplicateAuthorException(request.name(), request.nationality());
        Author author = new Author(request.name(), request.nationality(), request.birthYear());
        Author saved = authorRepository.save(author);
        return AuthorResponse.from(saved);
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        author.setName(request.name());
        author.setNationality(request.nationality());
        author.setBirthYear(request.birthYear());
        return AuthorResponse.from(authorRepository.save(author));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id))
            throw new AuthorNotFoundException(id);
        authorRepository.deleteById(id);
    }

    public boolean existsByNameAndNationality(String name, String nationality) {
        return authorRepository.existsByNameIgnoreCaseAndNationalityIgnoreCase(name, nationality);
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
