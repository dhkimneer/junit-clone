package com.meta.junitproject.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BookListRespDto {
    List<BookRespDto> items;

    @Builder
    public BookListRespDto(List<BookRespDto> bookList) {
        this.items = bookList;
    }
}
