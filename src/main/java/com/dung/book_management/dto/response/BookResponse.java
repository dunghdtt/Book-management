package com.dung.book_management.dto.response;

import com.dung.book_management.model.Book;

public record BookResponse(Long id, String title, String author, int publishedYear) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.id(), book.title(), book.author(), book.publishedYear());
    }
}
