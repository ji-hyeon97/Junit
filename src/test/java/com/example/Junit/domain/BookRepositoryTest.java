package com.example.Junit.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

//repository -> service -> controller 순서대로 테스트를 함
@ActiveProfiles("dev")
@DataJpaTest // DB와 관련된 컴포넌트들만 메모리에 로딩
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired //DI
    private BookRepository bookRepository;

    //@BeforeAll //테스트 시작전 한 번만 실행
    @BeforeEach //각 테스트 시작전에 한번씩 실행
    public void prepareData(){
        String title = "junit5";
        String author = "wlgus";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);
    } //트랜잭션 종료 돈 경우 어떻게 진행됬는지 ?
    // 1. [ 데이터 준비 + 책 등록 ] , [ 데이터 준비 + 책 목록보기 ] (부분) (참)
    // 2. [ 데이터 준비 + 책 등록 - 데이터 준비 + 책 목록보기 ] (전체) (거짓)


    // 1. 책 등록
    @Test
    public void 책등록_test(){
        //given(데이터 준비)
        String title = "junit5";
        String author = "wlgus";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        //when(테스트 실행)
        Book bookPersistence = bookRepository.save(book);

        //then(검증)
        assertEquals(title, bookPersistence.getTitle());
        assertEquals(author, bookPersistence.getAuthor());
    }

    // 2. 책 목록보기
    @Test
    public void 책목록보기_test(){
        // given
        String title = "junit5";
        String author = "wlgus";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        //bookRepository.save(book);

        // when
        List<Book> books = bookRepository.findAll();

        // then
        assertEquals(title, books.get(0).getTitle());
        assertEquals(author, books.get(0).getAuthor());
    }

    // 3. 책 한건 보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test(){
       //given
       String title = "junit5";
       String author = "wlgus";

       //when
        Book bookPs = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }

    // 4. 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);

        //then
        Optional<Book> bookPS = bookRepository.findById(id);

        assertFalse(bookPS.isPresent());
    }

    /**
     테스트 메서드 순서 보장 x -> @order 로 지정 가능//
     테스트 메서드 하나 실행 후 종료시 데이터가 초기화된다 -> @transactional 어노테이션
     primary key auto_increment 값이 초기화가 안됨 ->@Sql 어노테이션 사용
    */

    // 5. 책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test(){
        // given
        Long id = 1L;
        String title = "hi";
        String author = "zzzz";
        Book book = new Book(id,title,author);

        // when
        Book bookPS = bookRepository.save(book);
        bookRepository.findAll().stream().forEach((b)-> {
            System.out.println(b.getTitle());
        });

        Book bookPs = bookRepository.findById(id).get();

        // then
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }
}
