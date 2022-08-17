package com.meta.junitproject.service;

import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.util.MailSenderStub;
import com.meta.junitproject.web.dto.BookRespDto;
import com.meta.junitproject.web.dto.BookSaveReqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 문제점: 서비스만 테스트하고 싶은데 레포지토리 레이어가 함께 테스트된다는 점. (이렇게 해도 되기는 함)
 * 이미 레포지토리는 테스트 끝남.
 * 가짜 환경을 하나 만들고, 그 안에 가짜 repository, 가짜 MailSender를 넣자.
 * 그러면 레포지토리 올릴 필요 없음.
 * Mockito 환경(가짜 객체를 보관하는 환경) 필요
 */
@DataJpaTest
public class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void book_register_test() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("metacoding");

        // stub
        MailSenderStub mailSenderStub = new MailSenderStub();
        // 가짜로 bookRepository 만들기!!

        // when
        BookService bookService = new BookService(bookRepository, mailSenderStub);
        BookRespDto bookRespDto = bookService.register_book(dto);

        // then
        assertEquals(dto.getTitle(), bookRespDto.getTitle());
        assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
    }
}
