package com.dung.book_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dung.book_management.dto.ApiResponse;
import com.dung.book_management.service.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final BookService bookService;

    public StatsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> count() {
        return ResponseEntity.ok(ApiResponse.success("Đếm số sách thành công", bookService.count()));
    }

    // @GetMapping("/crash")
    // public String crash() {
    // throw new RuntimeException("Lỗi bí mật: mật khẩu database là 123456");
    // }
}
