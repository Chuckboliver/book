package com.chuckboliver.bookservice.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;

import java.time.chrono.ThaiBuddhistDate;

public record AddBookRequest(
        @NotEmpty(message = "title must not be empty")
        String title,

        @NotEmpty(message = "author must not be empty")
        String author,

        @PastOrPresent
        ThaiBuddhistDate publishedDate
) {
}
