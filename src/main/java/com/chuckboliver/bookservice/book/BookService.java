package com.chuckboliver.bookservice.book;

import java.util.List;

public interface BookService {
    List<Book> findByAuthor(String author);

    Book add(AddBookRequest book);
}
