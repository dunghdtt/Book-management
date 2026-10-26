package com.dung.book_management.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super("Khong tim thay tac gia co id " + id);
    }

}