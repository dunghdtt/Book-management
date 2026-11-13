package com.dung.book_management.dto.response;

public record BookResponse(Long id, String title, Long authorId, String authorName, int publishedYear) {

}
