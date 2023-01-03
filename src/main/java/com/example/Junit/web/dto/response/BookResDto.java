package com.example.Junit.web.dto.response;

import com.example.Junit.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BookResDto {
    private Long id;
    private String title;
    private String author;

    @Builder
    public BookResDto(Long id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
