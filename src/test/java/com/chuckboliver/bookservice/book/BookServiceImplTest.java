package com.chuckboliver.bookservice.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Nested
    @DisplayName("find all books by author")
    class FindAllBooksByAuthorTest {

        @Test
        @DisplayName("should find all books by author")
        void shouldFindAllBooksByAuthor() {
            final String authorName = "Patpum Hakaew";

            List<Book> expectedBooks = createBooks(authorName);

            when(bookRepository.findByAuthor(authorName)).thenReturn(createBooks(authorName));

            List<Book> actualBooks = bookService.findByAuthor(authorName);

            assertThat(actualBooks)
                    .isNotNull()
                    .hasSize(2)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(expectedBooks);

            verify(bookRepository).findByAuthor(authorName);
        }

        private List<Book> createBooks(String authorName) {
            return Arrays.asList(
                    Book.builder()
                            .id(1L)
                            .title("Lord of the Rings")
                            .author(authorName)
                            .publishedDate(LocalDate.now())
                            .build(),
                    Book.builder()
                            .id(2L)
                            .title("Harry Potter")
                            .author(authorName)
                            .publishedDate(LocalDate.now())
                            .build()
            );
        }

        @Test
        @DisplayName("should return an empty list when no books are found")
        void shouldReturnAnEmptyListWhenNoBooksAreFound() {
            final String authorName = "John";

            when(bookRepository.findByAuthor(authorName)).thenReturn(Collections.emptyList());

            List<Book> actualBooks = bookService.findByAuthor(authorName);

            assertThat(actualBooks)
                    .isNotNull()
                    .isEmpty();

            verify(bookRepository).findByAuthor(authorName);
        }

    }

    @Nested
    @DisplayName("add new book")
    class AddNewBookTest {

        @Test
        @DisplayName("should add new book")
        void shouldAddNewBook() {
            AddBookRequest addBookRequest = new AddBookRequest(
                    "Harry Potter",
                    "J.K. Rowling",
                    ThaiBuddhistDate.of(2540, 8, 12)
            );

            Book expectedBook = Book.builder()
                    .title("Harry Potter")
                    .author("J.K. Rowling")
                    .publishedDate(LocalDate.of(1997, 8, 12))
                    .build();

            bookService.add(addBookRequest);

            ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
            verify(bookRepository).save(bookArgumentCaptor.capture());

            Book book = bookArgumentCaptor.getValue();

            assertThat(book)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
        }
    }
}