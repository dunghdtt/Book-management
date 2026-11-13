package com.dung.book_management.mapper;

import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.model.Author;
import com.dung.book_management.model.Book;

public final class BookMapper {
    private BookMapper() {

    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(),
                book.getAuthor().getId(), book.getAuthor().getName(), book.getPublishedYear());
    }

    public static Book toEntity(BookRequest request, Author author) {
        return new Book(request.title(), author, request.publishedYear());
    }

    public static void updateEntity(Book book, BookRequest request, Author author) {
        book.setTitle(request.title());
        book.setAuthor(author);
        book.setPublishedYear(request.publishedYear());
    }
}
