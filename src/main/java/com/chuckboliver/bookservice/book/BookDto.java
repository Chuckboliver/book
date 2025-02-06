package com.chuckboliver.bookservice.book;

import java.time.chrono.ThaiBuddhistDate;

public record BookDto(
        Long id,
        String title,
        String author,
        ThaiBuddhistDate publishedDate
) {
}
