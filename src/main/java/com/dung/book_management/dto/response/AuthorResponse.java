package com.dung.book_management.dto.response;

import com.dung.book_management.model.Author;

public record AuthorResponse(Long id, String name, String nationality, int birthYear) {
    public static AuthorResponse from(Author author) {
        return new AuthorResponse(author.id(), author.name(), author.nationality(), author.birthYear());
    }
}
