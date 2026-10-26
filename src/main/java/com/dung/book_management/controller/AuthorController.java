package com.dung.book_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dung.book_management.dto.ApiResponse;
import com.dung.book_management.dto.request.AuthorRequest;
import com.dung.book_management.dto.response.AuthorResponse;
import com.dung.book_management.service.AuthorService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) Integer fromBirthYear,
            @RequestParam(required = false) Integer toBirthYear) {
        List<AuthorResponse> authors = authorService.findAll(keyword, nationality, fromBirthYear, toBirthYear);
        return ResponseEntity.ok(ApiResponse.success("lay danh sach thanh cong", authors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> getById(@PathVariable Long id) {
        AuthorResponse author = authorService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Lay tac gia thanh cong", author));
    }

    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getByNationality(@PathVariable String nationality) {
        List<AuthorResponse> authors = authorService.findByNationality(nationality);
        return ResponseEntity.ok(ApiResponse.success("Lay danh sach quoc tich thanh cong", authors));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponse>> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse saved = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Them tac gia thanh cong", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> update(@PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        AuthorResponse updated = authorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cap nhat tac gia thanh cong", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xoa tac gia thanh cong", null));
    }
}
