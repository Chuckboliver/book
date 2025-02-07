package com.chuckboliver.bookservice.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.chrono.ThaiBuddhistDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public ResponseEntity<List<BookDto>> findAllBooks(
            @NotEmpty(message = "author must not be empty")
            @RequestParam(required = false, value = "author") String author
    ) {
        List<Book> books = bookService.findByAuthor(author);
        List<BookDto> booksDto = books.stream()
                .map(v -> new BookDto(
                        v.getId(),
                        v.getTitle(),
                        v.getAuthor(),
                        v.getPublishedDate() != null ? ThaiBuddhistDate.from(v.getPublishedDate()) : null
                ))
                .toList();
        return ResponseEntity.ok(booksDto);
    }

    @PostMapping("")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody AddBookRequest addBookRequest) {
        Book createdBook = bookService.add(addBookRequest);

        BookDto bookDto = new BookDto(
                createdBook.getId(),
                createdBook.getTitle(),
                createdBook.getAuthor(),
                createdBook.getPublishedDate() != null ? ThaiBuddhistDate.from(createdBook.getPublishedDate()) : null
        );

        return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
    }

}
