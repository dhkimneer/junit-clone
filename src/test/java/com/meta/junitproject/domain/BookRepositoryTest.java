package com.meta.junitproject.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Controller <- Service <- Repository 순 테스트
 * 1. Repository: DB쪽 관련 테스트
 * 2. Service: 기능들이 트랜잭션을 잘 타는지
 * 3. Controller: 클라이언트와 테스트
 */

@DataJpaTest  // DB와 관련된 컴포넌트만 메모리에 로딩(Controller, Service는 메모리에 안 뜬다.)
public class BookRepositoryTest {

    @Autowired  // DI
    private BookRepository bookRepository;

    // 1. 책 등록
    @Test
    public void book_registration_test() {
        System.out.println("책 등록 테스트 실행");
    }

    // 2. 책 목록 보기

    // 3. 책 한 건 보기

    // 4. 책 수정

    // 5. 책 삭제
}
