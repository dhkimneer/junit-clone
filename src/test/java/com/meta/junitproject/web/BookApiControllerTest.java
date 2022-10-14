package com.meta.junitproject.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * json data - Dispatcher Servlet(BookSaveReqDto에 담기) - Controller(s)
 */
// 통합테스트(모든 layer 한 번에 테스트) - 가정 필요 없음 - stub 필요 없음
// 컨트롤러만 테스트하는 것이 아님
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach // 각 테스트 시작 전 한 번씩 실행
    public void data_ready() {
        String title = "junit";
        String author = "meta";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateBook_test() throws Exception {
        // given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("meta");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        assertThat(title).isEqualTo("spring");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test() {
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);

        // then
//        System.out.println("deleteBook_test() : " + response.getStatusCodeValue());
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");

        assertThat(code).isEqualTo(1);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test() { // 1. getBookOne_test 시작 전에 BeforeEach를 시작하는데 이 모든 것 전에 테이블 초기화 한 번 한다.
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Sql("classpath:db/tableInit.sql")  // 항상 auto-increment에 의한 오류 안 생김
    @Test
    public void getBookList_test() {
        // given

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Test
    public void saveBook_test() throws Exception {
        // given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("gettin");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("spring");
        assertThat(author).isEqualTo("gettin");
    }
}
