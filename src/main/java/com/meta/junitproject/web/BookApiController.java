package com.meta.junitproject.web;

import com.meta.junitproject.service.BookService;
import com.meta.junitproject.web.dto.response.BookRespDto;
import com.meta.junitproject.web.dto.request.BookSaveReqDto;
import com.meta.junitproject.web.dto.response.CommonRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    // final field가 어떤 클래스에 결합되어 있을 때 컴포지션 = has 관계
    private final BookService bookService;

    // 1. 책 등록
    // spring의 기본 parsing 전략; query string
    // json형식으로 받을 것임.
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> registerBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        // AOP 처리하는 것이 좋음
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe: bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("=========================");
            System.out.println(errorMap);
            System.out.println("=========================");

            throw new RuntimeException(errorMap.toString());
        }

        BookRespDto bookRespDto = bookService.register_book(bookSaveReqDto);
        return new ResponseEntity<>(CommonRespDto.builder().code(1).message("글 저장 성공").body(bookRespDto).build(),
                HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록 보기
    public ResponseEntity<?> getBookList() {
        return null;
    }

    // 3. 책 한 건 보기
    public ResponseEntity<?> getBookOne() {
        return null;
    }

    // 4. 책 삭제하기
    public ResponseEntity<?> deleteBook() {
        return null;
    }

    // 5. 책 수정하기
    public ResponseEntity<?> updateBook() {
        return null;
    }
}
