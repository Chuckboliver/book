package com.chuckboliver.bookservice.book;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book add(AddBookRequest book) {
        Book newBook = Book.builder()
                .title(book.title())
                .author(book.author())
                .publishedDate(LocalDate.from(book.publishedDate()))
                .build();

        return bookRepository.save(newBook);
    }
}
