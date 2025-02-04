package com.chuckboliver.bookservice.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(
        scripts = {
                "classpath:data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        statements = {
                "truncate table book"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /books?author={author}")
    class FindAllBooksByAuthorEndpointIT {

        @Test
        @DisplayName("should return all books with specified author")
        void shouldReturnAllBooksWithSpecifiedAuthor() throws Exception {
            mockMvc.perform(
                            get("/books")
                                    .param("author", "Alex Michaelides")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].title").value("The Silent Patient"))
                    .andExpect(jsonPath("$[0].author").value("Alex Michaelides"))
                    .andExpect(jsonPath("$[0].publishedDate").value("2562-02-05"))
            ;
        }

        @Test
        @DisplayName("should response bad request when author is empty")
        void shouldResponseBadRequestWhenAuthorIsEmpty() throws Exception {
            mockMvc.perform(
                            get("/books")
                                    .param("author", "")
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fieldErrors").isNotEmpty())
                    .andExpect(jsonPath("$.fieldErrors[0].field").value("findAllBooks.author"))
                    .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(""))
                    .andExpect(jsonPath("$.fieldErrors[0].message").value("author must not be empty"));
        }
    }

    @Nested
    @DisplayName("POST /books")
    class AddBookEndpointIT {

        @Test
        @DisplayName("should add book")
        void shouldAddBook() throws Exception {
            AddBookRequest addBookRequest = new AddBookRequest(
                    "Animal1",
                    "Sam",
                    ThaiBuddhistDate.of(2544, 1, 1)
            );

            MvcResult mvcResult = mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("Animal1"))
                    .andExpect(jsonPath("$.author").value("Sam"))
                    .andExpect(jsonPath("$.publishedDate").value("2544-01-01"))
                    .andReturn();

            BookDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

            Book expectedBook = Book.builder()
                    .id(response.getId())
                    .title("Animal1")
                    .author("Sam")
                    .publishedDate(LocalDate.of(2001, 1, 1))
                    .build();

            Optional<Book> optionalBook = bookRepository.findById(response.getId());
            assertThat(optionalBook)
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
        }

        @Test
        @DisplayName("should add book when published date is present")
        void shouldAddBookWhenPublishedDateIsPresent() throws Exception {
            ThaiBuddhistDate currentThaiBuddhistDate = ThaiBuddhistDate.now();
            String expectedCurrentThaiBuddhistDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .format(currentThaiBuddhistDate);

            AddBookRequest addBookRequest = new AddBookRequest(
                    "Cooking2025",
                    "Joe",
                    currentThaiBuddhistDate
            );

            mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.title").value("Cooking2025"))
                    .andExpect(jsonPath("$.author").value("Joe"))
                    .andExpect(jsonPath("$.publishedDate").value(expectedCurrentThaiBuddhistDate));
        }

        @Test
        @DisplayName("should response bad request when published date is in the future")
        void shouldResponseBadRequestWhenPublishedDateIsInTheFuture() throws Exception {
            ThaiBuddhistDate tomorrowThaiBuddhistDate = ThaiBuddhistDate.now().plus(1, ChronoUnit.DAYS);
            String expectedTomorrowFormattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .format(tomorrowThaiBuddhistDate);

            AddBookRequest addBookRequest = new AddBookRequest(
                    "Programming2025",
                    "Dennis",
                    tomorrowThaiBuddhistDate
            );

            mockMvc.perform(
                            post("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(addBookRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fieldErrors").isNotEmpty())
                    .andExpect(jsonPath("$.fieldErrors[0].field").value("publishedDate"))
                    .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(expectedTomorrowFormattedDate))
                    .andExpect(jsonPath("$.fieldErrors[0].message").value("must be a date in the past or in the present"))
            ;
        }
    }

}
