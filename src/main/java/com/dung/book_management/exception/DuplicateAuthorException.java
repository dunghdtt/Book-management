package com.dung.book_management.exception;

public class DuplicateAuthorException extends RuntimeException {
    public DuplicateAuthorException(String name, String nationality) {
        super("Tac gia " + name + " " + " quoc tich " + nationality + " da ton tai");
    }
}
