package com.example.Junit.web;

import com.example.Junit.domain.Book;
import com.example.Junit.domain.BookRepository;
import com.example.Junit.service.BookService;
import com.example.Junit.web.dto.request.BookSaveReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach //각 테스트 시작전에 한번씩 실행
    public void prepareData(){
        String title = "junit5";
        String author = "wlgus";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);
    }

    @Test
    @DisplayName("책 저장하기 테스트")
    public void saveBook_Test() throws Exception{
        // given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("제목");
        bookSaveReqDto.setAuthor("저자");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book",HttpMethod.POST, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("제목");
        assertThat(author).isEqualTo("저자");
    }

    @Test
    @DisplayName("책 목록보기 테스트")
    @Sql("classpath:db/tableInit.sql")
    public void getBookList_test(){
        // given

        // when
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book",HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String titleTest = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(titleTest).isEqualTo("junit5");
    }

    @Test
    @DisplayName("책 한건보기 테스트 ")
    @Sql("classpath:db/tableInit.sql")
    public void getBookOne_test(){
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id,HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String titleTest = dc.read("$.body.title");
        assertThat(code).isEqualTo(1);
        assertThat(titleTest).isEqualTo("junit5");
    }

    @Test
    @DisplayName("책 삭제하기 테스트 ")
    @Sql("classpath:db/tableInit.sql")
    public void delete_book(){
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.DELETE, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        assertThat(code).isEqualTo(1);
    }

    @Test
    @DisplayName("책 수정하기 테스트 ")
    @Sql("classpath:db/tableInit.sql")
    public void updateBook_test() throws Exception{
        // given
        Integer id = 1;

        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("변경후 제목");
        bookSaveReqDto.setAuthor("변경후 저자");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.PUT, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String titleTest = dc.read("$.body.title");
        assertThat(titleTest).isEqualTo("변경후 제목");
    }
}
