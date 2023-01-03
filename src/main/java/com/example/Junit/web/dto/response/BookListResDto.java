package com.example.Junit.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BookListResDto {
    List<BookResDto> items;

    @Builder
    public BookListResDto(List<BookResDto> bookResDtoList) {
        this.items = bookResDtoList;
    }
}
