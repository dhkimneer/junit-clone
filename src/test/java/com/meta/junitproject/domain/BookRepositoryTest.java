package com.meta.junitproject.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Controller <- Service <- Repository 순 테스트
 * 1. Repository: DB쪽 관련 테스트
 * 2. Service: 기능들이 트랜잭션을 잘 타는지
 * 3. Controller: 클라이언트와 테스트
 */

/**
 * [테스트를 왜 하는가?]
 * 1. 메서드가 하나의 기능만을 담당하도록 하면 책임을 분리시킬 수 있다.
 * 2. 각 기능마다 테스트 코드를 작성하면 유지, 보수를 하기 쉽다.
 * 3. 시간 단축이 가능하다.
 */

/**
 * [Junit test]
 * 1. 테스트 메서드 실행 순서 보장이 안 된다. -> @Order(1) 같은 어노테이션 붙여야 순서 보장.
 * 2. 테스트 메서드가 하나 실행 후 종료되면 데이터 초기화. by @Transactional
 * 그런데 pk(auto-increment)값이 초기화가 안 된다. -> @Sql, @BeforeEach(alter sql문 직접 실행해 auto-increment 초기화)
 */

@DataJpaTest  // DB와 관련된 컴포넌트만 메모리에 로딩(Controller, Service는 메모리에 안 뜬다.)
public class BookRepositoryTest {

    @Autowired  // DI
    private BookRepository bookRepository;

//    @BeforeAll // 테스트 시작 전 한 번만 실행
    @BeforeEach // 각 테스트 시작 전 한 번씩 실행
    public void data_ready() {
        String title = "junit";
        String author = "meta";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    } // 만약 트랜잭션이 종료되었다면 2번이 작동하지 않아야 함.
    // 가정 1. [data_ready() + 1번], [data_ready() + 2번] -> size 1 (이게 맞다.)
    // 가정 2. [data_ready() + 1번 + data_ready() + 2번] -> size 2

    // 1. 책 등록
    @Test
    public void book_registration_test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "meta";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        Book bookPS = bookRepository.save(book); // DB에 저장된 북이므로 영속화된 데이터

        // then (검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터 초기화)

    // 2. 책 목록 보기
    @Test
    public void book_contents_test() {
        //given
        String title = "junit";
        String author = "meta";

        //when
        List<Book> booksPS = bookRepository.findAll();

        System.out.println("사이즈: ==================== : " + booksPS.size());

        //then
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    // 3. 책 한 건 보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void read_one_book_test() {
        //given
        String title = "junit";
        String author = "meta";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql") // id 찾는 모든 메서드 앞에는 붙여주는 것이 좋다.
    @Test
    public void delete_book() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    // 1, junit, meta
    // 5. 책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void modify_book() {
        // given
        Long id = 1L;
        String title = "junit5";
        String author = "meta2";
        Book book = new Book(id, title, author);

//        bookRepository.findAll()
//                .forEach((b) -> {
//                    System.out.println(b.getId());
//                    System.out.println(b.getTitle());
//                    System.out.println(b.getAuthor());
//                    System.out.println("1. ==========================");
//                });

        // when
        Book bookPS = bookRepository.save(book);

//        bookRepository.findAll()
//                .forEach((b) -> {
//                    System.out.println(b.getId());
//                    System.out.println(b.getTitle());
//                    System.out.println(b.getAuthor());
//                    System.out.println("2. ==========================");
//                });

        // then
        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }
}
