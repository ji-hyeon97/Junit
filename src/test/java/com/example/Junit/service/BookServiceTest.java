package com.example.Junit.service;

import com.example.Junit.domain.Book;
import com.example.Junit.domain.BookRepository;
import com.example.Junit.util.MailSender;
import com.example.Junit.web.dto.response.BookListResDto;
import com.example.Junit.web.dto.response.BookResDto;
import com.example.Junit.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @Test
    public void 책등록하기_test(){

        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring");
        dto.setAuthor("seo");

        //stub (가설) 가짜 환경에서 실행하기 때문에
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        BookResDto bookResDto = bookService.책등록(dto);

        //then
        assertEquals(dto.getTitle(), bookResDto.getTitle());
        assertEquals(dto.getAuthor(), bookResDto.getAuthor());
        assertThat(bookResDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

    @Test
    public void 책목록보기_test(){
        //given

        //stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"junit","seo"));
        books.add(new Book(2L,"1234","ji"));

        when(bookRepository.findAll()).thenReturn(books);

        //when
        BookListResDto bookRespDto = bookService.책목록();

        //then
        assertThat(bookRespDto.getItems().get(0).getTitle()).isEqualTo("junit");
        assertThat(bookRespDto.getItems().get(0).getAuthor()).isEqualTo("seo");
        assertThat(bookRespDto.getItems().get(1).getTitle()).isEqualTo("1234");
        assertThat(bookRespDto.getItems().get(1).getAuthor()).isEqualTo("ji");
    }

    @Test
    public void 책한건보기_test(){
        // given
        Long id = 1L;
        Book book = new Book(1L, "junit","메타코딩");
        Optional<Book> bookOP = Optional.of(book);

        // stub
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when®
        BookResDto bookResDto = bookService.책하나(id);

        // then
        assertThat(bookResDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void 책수정하기_test(){
        // given
        Long id  = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("before");
        dto.setAuthor("지현");

        // stub
        Book book = new Book(1L,"after","지현1");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookResDto bookResDto = bookService.책수정(id, dto);

        // then
        assertThat(bookResDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(book.getAuthor());
    }
}
