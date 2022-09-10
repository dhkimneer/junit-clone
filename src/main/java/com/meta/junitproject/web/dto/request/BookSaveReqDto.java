package com.meta.junitproject.web.dto.request;

import com.meta.junitproject.domain.Book;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter  // Controller에서 setter가 호출되면서 DTO에 값이 채워짐.
public class BookSaveReqDto {

    @Size(min = 1, max = 50)
    @NotBlank  // null과 공백 검사
    private String title;

    @Size(min = 2, max = 20)
    @NotBlank
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}