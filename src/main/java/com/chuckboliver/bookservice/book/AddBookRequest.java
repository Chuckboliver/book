package com.chuckboliver.bookservice.book;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.chrono.ThaiBuddhistDate;

@Value
public class AddBookRequest {
    @NotEmpty(message = "title must not be empty")
    String title;

    @NotEmpty(message = "author must not be empty")
    String author;

    @PastOrPresent
    ThaiBuddhistDate publishedDate;
}
