package com.meta.junitproject.service;

import com.meta.junitproject.domain.Book;
import com.meta.junitproject.domain.BookRepository;
import com.meta.junitproject.util.MailSender;
import com.meta.junitproject.web.dto.response.BookListRespDto;
import com.meta.junitproject.web.dto.response.BookRespDto;
import com.meta.junitproject.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 문제점: 서비스만 테스트하고 싶은데 레포지토리 레이어가 함께 테스트된다는 점. (이렇게 해도 되기는 함)
 * 이미 레포지토리는 테스트 끝남.
 * 가짜 환경을 하나 만들고, 그 안에 가짜 repository, 가짜 MailSender를 넣자.
 * 그러면 레포지토리 올릴 필요 없음.
 * Mockito 환경(가짜 객체를 보관하는 환경) 필요 / 가짜 환경을 만드는게 Mockito Extension
 * 얘들을 mock으로 띄우고, bookService에 @InjectMocks를 걸어주면 bookService가 new되면서 mock들을 injection
 * 실제 서비스 실행시에는 가짜 객체가 들어가 동작을 제대로 안 할 것임.
 * 그래서 stub 정의해주면 잘 동작함.
 *
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    // @Autowired 사용 불가
    @InjectMocks // mock들을 주입한다.
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @Test
    public void book_register_test() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("metacoding");

        // stub (행동 정의, 가설)
        // save할 때 가짜가 호출되며 any가 들어감, 그 반환값만 정의하면 됨.
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto bookRespDto = bookService.register_book(dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

    @Test
    public void look_book_contents() {
        // given(파라미터로 들어올 데이터)

        // stub(가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit강의", "메타코딩"));
        books.add(new Book(2L, "spring강의", "겟인데어"));
        when(bookRepository.findAll()).thenReturn(books);

        // when(실행)
        BookListRespDto bookListRespDto = bookService.look_book_contents();

        // then(검증)
        assertThat(bookListRespDto.getItems().get(0).getTitle()).isEqualTo("junit강의");
        assertThat(bookListRespDto.getItems().get(0).getAuthor()).isEqualTo("메타코딩");
        assertThat(bookListRespDto.getItems().get(1).getTitle()).isEqualTo("spring강의");
        assertThat(bookListRespDto.getItems().get(1).getAuthor()).isEqualTo("겟인데어");
    }

    @Test
    public void look_one_book() {
        // given
        Long id = 1L;

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.look_one_book(id);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void modify_book() {
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring강의");
        dto.setAuthor("겟인데어");

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.modify_book(id, dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }
}
