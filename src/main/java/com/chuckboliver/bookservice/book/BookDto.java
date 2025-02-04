package com.chuckboliver.bookservice.book;

import lombok.Value;

import java.time.chrono.ThaiBuddhistDate;

@Value
public class BookDto {
    Long id;
    String title;
    String author;
    ThaiBuddhistDate publishedDate;
}
