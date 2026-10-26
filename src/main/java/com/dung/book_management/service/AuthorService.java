package com.dung.book_management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dung.book_management.dto.request.AuthorRequest;
import com.dung.book_management.dto.response.AuthorResponse;
import com.dung.book_management.exception.AuthorNotFoundException;
import com.dung.book_management.model.Author;

@Service
public class AuthorService {
    private final List<Author> authors = new ArrayList<>(List.of(
            new Author(1L, "Robert C. Martin", "USA", 1952),
            new Author(2L, "Joshua Bloch", "USA", 1961),
            new Author(3L, "Kathy Sierra", "USA", 1957)));
    private long nextId = 4;

    public AuthorService() {
        System.out.println("AuthorService duoc tao");
    }

    public int count() {
        return authors.size();
    }

    public List<AuthorResponse> findAll(String keyword, String nationality, Integer fromBirthYear,
            Integer toBirthYear) {
        return authors.stream()
                .filter(a -> keyword == null || keyword.isBlank()
                        || a.name().toLowerCase().contains(keyword.toLowerCase()))
                .filter(a -> nationality == null || nationality.isBlank()
                        || a.nationality().toLowerCase().contains(nationality.toLowerCase()))
                .filter(a -> fromBirthYear == null || a.birthYear() >= fromBirthYear)
                .filter(a -> toBirthYear == null || a.birthYear() <= toBirthYear)
                .map(a -> AuthorResponse.from(a))
                .toList();
    }

    public AuthorResponse findById(Long id) {
        return authors.stream()
                .filter(a -> a.id().equals(id))
                .findFirst()
                .map(a -> AuthorResponse.from(a))
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<AuthorResponse> findByNationality(String nationality) {
        return authors.stream()
                .filter(a -> a.nationality().equalsIgnoreCase(nationality))
                .map(a -> AuthorResponse.from(a))
                .toList();
    }

    public AuthorResponse create(AuthorRequest request) {
        Author saved = new Author(nextId++, request.name(), request.nationality(), request.birthYear());
        authors.add(saved);
        return AuthorResponse.from(saved);
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).id().equals(id)) {
                Author updated = new Author(id, request.name(), request.nationality(), request.birthYear());
                authors.set(i, updated);
                return AuthorResponse.from(updated);
            }
        }
        throw new AuthorNotFoundException(id);
    }

    public void delete(Long id) {
        boolean removed = authors.removeIf(a -> a.id().equals(id));
        if (!removed)
            throw new AuthorNotFoundException(id);
    }

    public boolean existsByTitleAndAuthor(String author) {
        return authors.stream().anyMatch(
                a -> a.name().equalsIgnoreCase(author));
    }
}
