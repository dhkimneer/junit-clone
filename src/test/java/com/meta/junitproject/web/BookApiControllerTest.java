package com.meta.junitproject.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.meta.junitproject.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * json data - Dispatcher Servlet(BookSaveReqDto에 담기) - Controller(s)
 */
// 통합테스트(모든 layer 한 번에 테스트) - 가정 필요 없음 - stub 필요 없음
// 컨트롤러만 테스트하는 것이 아님
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
