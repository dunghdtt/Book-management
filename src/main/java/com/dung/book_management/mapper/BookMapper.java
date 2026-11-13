package com.dung.book_management.mapper;

import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.model.Book;

public final class BookMapper {
    private BookMapper() {

    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getPublishedYear());
    }

    public static Book toEntity(BookRequest request) {
        return new Book(request.title(), request.author(), request.publishedYear());
    }

    public static void updateEntity(Book book, BookRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPublishedYear(request.publishedYear());
    }
}
