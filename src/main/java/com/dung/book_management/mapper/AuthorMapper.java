package com.dung.book_management.mapper;

import com.dung.book_management.dto.request.AuthorRequest;
import com.dung.book_management.dto.response.AuthorResponse;
import com.dung.book_management.model.Author;

public final class AuthorMapper {
    private AuthorMapper() {

    }

    public static AuthorResponse toResponse(Author author) {
        return new AuthorResponse(author.getId(), author.getName(), author.getNationality(), author.getBirthYear());
    }

    public static Author toEntity(AuthorRequest request) {
        return new Author(request.name(), request.nationality(), request.birthYear());
    }

    public static void updateEntity(Author author, AuthorRequest request) {
        author.setName(request.name());
        author.setNationality(request.nationality());
        author.setBirthYear(request.birthYear());
    }
}
