package com.dung.book_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorRequest(
        @NotBlank(message = "Ten tac gia khong duoc de trong") String name,
        @NotBlank(message = "Ten quoc tich khong duoc de trong") String nationality,
        @NotNull(message = "Nam sinh khong duoc de trong") @Min(value = 1000, message = "Nam sinh phai tu 1000 tro len") Integer birthYear) {

}
