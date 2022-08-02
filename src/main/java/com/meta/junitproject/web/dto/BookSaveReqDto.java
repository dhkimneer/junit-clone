package com.meta.junitproject.web.dto;

import com.meta.junitproject.domain.Book;
import lombok.Setter;

@Setter  // Controller에서 setter가 호출되면서 DTO에 값이 채워짐.
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}