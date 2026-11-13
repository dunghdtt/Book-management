package com.dung.book_management.exception;

public class AuthorHasBooksException extends RuntimeException {
    public AuthorHasBooksException(Long id) {
        super("Khong the xoa tac gia co id " + id + " vi van con sach");
    }

}
