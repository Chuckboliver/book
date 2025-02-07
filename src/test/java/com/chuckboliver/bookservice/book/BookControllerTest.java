package com.chuckboliver.bookservice.book;

import com.chuckboliver.bookservice.config.JacksonConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import({
        JacksonConfig.class
})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Nested
    @DisplayName("GET /books?author={author}")
    class FindAllBooksByAuthorTest {

        @Test
        @DisplayName("should return books by specific author")
        void shouldReturnBooksBySpecificAuthor() throws Exception {
            final String author = "J.K. Rowling";
            List<Book> books = Arrays.asList(
                    Book.builder()
                            .id(1L)
                            .title("Harry Potter")
                            .author(author)
                            .publishedDate(LocalDate.of(1997, 6, 26))
                            .build(),
                    Book.builder()
                            .id(2L)
                            .title("Java Programming")
                            .author(author)
                            .publishedDate(LocalDate.of(2002, 8, 12))
                            .build()
            );

            when(bookService.findByAuthor(author)).thenReturn(books);

            mockMvc.perform(
                            get("/books")
                                    .param("author", author)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].title").value("Harry Potter"))
                    .andExpect(jsonPath("$[0].author").value(author))
                    .andExpect(jsonPath("$[0].publishedDate").value("2540-06-26"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].title").value("Java Programming"))
                    .andExpect(jsonPath("$[1].author").value(author))
                    .andExpect(jsonPath("$[1].publishedDate").value("2545-08-12"))
            ;

            verify(bookService).findByAuthor(author);
        }

        @Test
        @DisplayName("should return an empty list when no books are found")
        void shouldReturnAnEmptyListWhenNoBooksAreFound() throws Exception {
            final String author = "J.K. Rowling";

            when(bookService.findByAuthor(author)).thenReturn(Collections.emptyList());

            mockMvc.perform(
                            get("/books")
                                    .param("author", author)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isEmpty());

            verify(bookService).findByAuthor(author);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("should response bad request when author is blank")
        void shouldResponseBadRequestWhenAuthorIsBlank(String author) throws Exception {
            mockMvc.perform(
                            get("/books")
                                    .param("author", author)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fieldErrors").isNotEmpty())
                    .andExpect(jsonPath("$.fieldErrors[0].field").value("findAllBooks.author"))
                    .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(author))
                    .andExpect(jsonPath("$.fieldErrors[0].message").value("author must not be empty"));
        }
    }

    @Nested
    @DisplayName("POST /books")
    class AddBookTest {

        @Test
        @DisplayName("should response ok when add book successful")
        void shouldResponseOkWhenAddBookSuccessful() throws Exception {
            AddBookRequest addBookRequest = new AddBookRequest(
                    "Ultraman",
                    "Paul",
                    ThaiBuddhistDate.of(2542, 6, 26)
            );

            when(bookService.add(addBookRequest)).thenReturn(
                    Book.builder()
                            .id(1L)
                            .title("Ultraman")
                            .author("Paul")
                            .publishedDate(LocalDate.of(1999, 6, 26))
                            .build()
            );

            mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Ultraman"))
                    .andExpect(jsonPath("$.author").value("Paul"))
                    .andExpect(jsonPath("$.publishedDate").value("2542-06-26"));

            verify(bookService).add(addBookRequest);
        }

        @ParameterizedTest
        @ArgumentsSource(NullOrEmptyTitleAddBookRequest.class)
        @DisplayName("should response bad request when title is null or empty")
        void shouldResponseBadRequestWhenTitleIsNullOrEmpty(AddBookRequest addBookRequest) throws Exception {
            mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fieldErrors").isNotEmpty())
                    .andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
                    .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(addBookRequest.title()))
                    .andExpect(jsonPath("$.fieldErrors[0].message").value("title must not be empty"));

            verifyNoInteractions(bookService);
        }

        @ParameterizedTest
        @ArgumentsSource(NullOrEmptyAuthorAddBookRequest.class)
        @DisplayName("should response bad request when author is null or empty")
        void shouldResponseBadRequestWhenAuthorIsNullOrEmpty(AddBookRequest addBookRequest) throws Exception {
            mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fieldErrors").isNotEmpty())
                    .andExpect(jsonPath("$.fieldErrors[0].field").value("author"))
                    .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(addBookRequest.author()))
                    .andExpect(jsonPath("$.fieldErrors[0].message").value("author must not be empty"));

            verifyNoInteractions(bookService);
        }
    }

    static class NullOrEmptyTitleAddBookRequest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    arguments(
                            new AddBookRequest(null, "John", ThaiBuddhistDate.of(2541, 12, 10))
                    ),
                    arguments(
                            new AddBookRequest("", "John", ThaiBuddhistDate.of(2541, 12, 10))
                    )
            );
        }
    }

    static class NullOrEmptyAuthorAddBookRequest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    arguments(
                            new AddBookRequest("Harry Potter", null, ThaiBuddhistDate.of(2541, 12, 10))
                    ),
                    arguments(
                            new AddBookRequest("Lord of the Rings", "", ThaiBuddhistDate.of(2541, 12, 10))
                    )
            );
        }
    }
}
