package com.dung.book_management.controller;

import com.dung.book_management.service.AuthorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dung.book_management.dto.ApiResponse;
import com.dung.book_management.service.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final AuthorService authorService;
    private final BookService bookService;

    public StatsController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> count() {
        return ResponseEntity.ok(ApiResponse.success("Đếm số sách thành công", bookService.count()));
    }

    @GetMapping("/authors/count")
    public ResponseEntity<ApiResponse<Integer>> countAuthor() {
        return ResponseEntity.ok(ApiResponse.success("Đếm thành công", authorService.count()));
    }

    // @GetMapping("/crash")
    // public String crash() {
    // throw new RuntimeException("Lỗi bí mật: mật khẩu database là 123456");
    // }
}
