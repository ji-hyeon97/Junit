package com.example.Junit.domain;

import com.example.Junit.web.dto.response.BookResDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50, nullable = false)
    private String title;

    @Column(length=20, nullable = false)
    private String author;

    @Builder //순서 상관 x
    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    public void update(String title, String author){
        this.title = title;
        this.author = author;
    }

    public BookResDto toDto(){
        return BookResDto.builder()
                .id(id)
                .title(title)
                .author(author)
                .build();
    }
}
