package com.dung.book_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
                @NotBlank(message = "Ten sach khong duoc de trong") String title,
                @NotBlank(message = "Tac gia khong duoc de trong") String author,
                @NotNull(message = "Nam san xuat khong duoc de trong") @Min(value = 1450, message = "Nam xuat ban phai tu 1450 tro len") Integer publishedYear) {

}
