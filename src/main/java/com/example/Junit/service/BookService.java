package com.example.Junit.service;

import com.example.Junit.domain.Book;
import com.example.Junit.domain.BookRepository;
import com.example.Junit.util.MailSender;
import com.example.Junit.web.dto.BookResDto;
import com.example.Junit.web.dto.BookSaveReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 1. 책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResDto 책등록(BookSaveReqDto dto){ // dto 로 데이터를 받고 save 치고 영속화 객체는 service 에서만 있도록 ! controller 에 가면 안됨
        Book bookPS = bookRepository.save(dto.toEntity());
        if(bookPS != null){
            if(!mailSender.send()){
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return bookPS.toDto();
    }

    // 2. 책목록보기
    public List<BookResDto> 책목록(){
        return bookRepository.findAll().stream() //stream: 데이터를 담음, map: 다른 타입으로 변경 후 리턴 가능(stream 을 복제)
                .map(Book::toDto)
                .collect(Collectors.toList()); // 리스트로 변환
    }

    // 3. 책 한건 보기
    public BookResDto 책하나(Long id){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){
            Book bookPS = bookOP.get();
            return bookPS.toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    // 4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void 책삭제(Long id){
        bookRepository.deleteById(id);
    }

    // 5. 책 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResDto 책수정(Long id, BookSaveReqDto dto){ // id, title, author
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(),dto.getAuthor()); // 더티체킹
            return bookPS.toDto();
        }else{
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }
}
