package com.dung.book_management.service;

import java.util.ArrayList;
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
        return authorRepository.findAll().stream()
                .filter(a -> keyword == null || keyword.isBlank()
                        || a.getName().toLowerCase().contains(keyword.toLowerCase()))
                .filter(a -> nationality == null || nationality.isBlank()
                        || a.getNationality().toLowerCase().contains(nationality.toLowerCase()))
                .filter(a -> fromBirthYear == null || a.getBirthYear() >= fromBirthYear)
                .filter(a -> toBirthYear == null || a.getBirthYear() <= toBirthYear)
                .map(a -> AuthorResponse.from(a))
                .toList();
    }

    public AuthorResponse findById(Long id) {
        return authorRepository.findById(id)
                .map(a -> AuthorResponse.from(a))
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<AuthorResponse> findByNationality(String nationality) {
        return authorRepository.findAll().stream()
                .filter(a -> a.getNationality().equalsIgnoreCase(nationality))
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
        return authorRepository.findAll().stream().anyMatch(
                a -> a.getName().equalsIgnoreCase(name) && a.getNationality().equalsIgnoreCase(nationality));
    }
}
