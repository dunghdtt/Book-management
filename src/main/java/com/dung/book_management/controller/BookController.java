package com.dung.book_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dung.book_management.dto.ApiResponse;
import com.dung.book_management.dto.request.BookRequest;
import com.dung.book_management.dto.response.BookResponse;
import com.dung.book_management.service.BookService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/books")
public class BookController {
        private final BookService bookService;

        public BookController(BookService bookService) {
                this.bookService = bookService;
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<BookResponse>>> getAll(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Integer fromYear,
                        @RequestParam(required = false) Integer toYear,
                        @RequestParam(required = false) String author) {
                List<BookResponse> books = bookService.findAll(keyword, fromYear, toYear, author);
                return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sách thành công", books));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<BookResponse>> getById(@PathVariable Long id) {
                BookResponse book = bookService.findById(id);
                return ResponseEntity.ok(ApiResponse.success("Lấy sách thành công", book));// 200
        }

        @GetMapping("/author/{author}")
        public ResponseEntity<ApiResponse<List<BookResponse>>> getByAuthor(@PathVariable String author) {
                List<BookResponse> books = bookService.findByAuthor(author);
                return ResponseEntity.ok(ApiResponse.success("Lấy sách theo tác giả thành công", books));
        }

        @PostMapping
        public ResponseEntity<ApiResponse<BookResponse>> create(@Valid @RequestBody BookRequest request) {
                BookResponse saved = bookService.create(request);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Thêm sách thành công", saved));// 201
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<BookResponse>> update(@PathVariable Long id,
                        @Valid @RequestBody BookRequest request) {
                BookResponse updated = bookService.update(id, request);
                return ResponseEntity.ok(ApiResponse.success("Cập nhật sách thành công", updated));// 200
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                bookService.delete(id);
                return ResponseEntity.ok(ApiResponse.success("Xoá sách thành công", null));// 204
        }

}
