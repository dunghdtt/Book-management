# Book Management API

REST API quản lý sách và tác giả, xây dựng bằng Spring Boot.
Dự án thực hành Phase 1 trong lộ trình học Backend Java của mình.

## Công nghệ

- Java 17, Spring Boot 4.1.1, Maven
- Spring Web (REST API)
- Bean Validation (`@Valid`, `@NotBlank`, `@Min`...)
- Xử lý lỗi tập trung với `@RestControllerAdvice`
- Dữ liệu lưu tạm trong bộ nhớ (chưa dùng database)

## Chức năng

- Quản lý sách: xem, tìm kiếm, lọc theo tác giả và năm xuất bản, thêm, sửa, xoá
- Quản lý tác giả: xem , tìm kiếm lọc theo tên tác giả, thêm, sửa, xóa
- Kiểm tra dữ liệu đầu vào, báo lỗi rõ ràng
- Mọi response theo một định dạng thống nhất

## Cách chạy

## Cách chạy

Yêu cầu: JDK 17 trở lên.

```bash
git clone https://github.com/dunghdtt/Book-management.git
cd Book-management
./mvnw spring-boot:run          # Windows: .\mvnw.cmd spring-boot:run
```

Ứng dụng chạy tại `http://localhost:8081`

## Danh sách API

### Sách: `/api/books`

| Method | URL                          | Mô tả                                                                           |
| ------ | ---------------------------- | ------------------------------------------------------------------------------- |
| GET    | `/api/books`                 | Lấy danh sách sách. Tham số tuỳ chọn: `keyword`, `author`, `fromYear`, `toYear` |
| GET    | `/api/books/{id}`            | Xem một cuốn sách                                                               |
| GET    | `/api/books/author/{author}` | Lấy sách theo tên tác giả                                                       |
| POST   | `/api/books`                 | Thêm sách                                                                       |
| PUT    | `/api/books/{id}`            | Cập nhật sách                                                                   |
| DELETE | `/api/books/{id}`            | Xoá sách                                                                        |

### Tác giả: `/api/authors`

| Method | URL                                      | Mô tả                                                                                             |
| ------ | ---------------------------------------- | ------------------------------------------------------------------------------------------------- |
| GET    | `/api/authors`                           | Lấy danh sách tác giả. Tham số tuỳ chọn: `keyword`, `nationality`, `fromBirthYear`, `toBirthYear` |
| GET    | `/api/authors/{id}`                      | Xem một tác giả                                                                                   |
| GET    | `/api/authors/nationality/{nationality}` | Lấy tác giả theo quốc tịch                                                                        |
| POST   | `/api/authors`                           | Thêm tác giả                                                                                      |
| PUT    | `/api/authors/{id}`                      | Cập nhật tác giả                                                                                  |
| DELETE | `/api/authors/{id}`                      | Xoá tác giả                                                                                       |

### Thống kê: `/api/stats`

| Method | URL                        | Mô tả          |
| ------ | -------------------------- | -------------- |
| GET    | `/api/stats/count`         | Đếm số sách    |
| GET    | `/api/stats/authors/count` | Đếm số tác giả |

## Định dạng response

## Ví dụ

### Thêm sách

`POST /api/books`

```json
{ "title": "Refactoring", "author": "Martin Fowler", "publishedYear": 2018 }
```

### Response thành công (201)

```json
(dán kết quả thật từ Postman vào đây)
```

### Response lỗi (400)

```json
(dán kết quả thật khi gửi tên sách trống vào đây)
```

## Mã lỗi

| Mã  | Khi nào                                   |
| --- | ----------------------------------------- |
| 400 | Dữ liệu gửi lên không hợp lệ              |
| 404 | Không tìm thấy sách / tác giả / đường dẫn |
| 409 | Sách hoặc tác giả đã tồn tại              |
| 500 | Lỗi hệ thống                              |
