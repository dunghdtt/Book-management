package com.dung.book_management.exception;

public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String title, String author) {
        super("Sach " + title + " " + " cua " + author + " da ton tai");
    }
}
