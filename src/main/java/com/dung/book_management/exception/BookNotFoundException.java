package com.dung.book_management.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Khong tim thay sach co id " + id);
    }

}