package com.example.Junit.web;

import com.example.Junit.service.BookService;
import com.example.Junit.web.dto.response.BookListResDto;
import com.example.Junit.web.dto.response.BookResDto;
import com.example.Junit.web.dto.request.BookSaveReqDto;
import com.example.Junit.web.dto.response.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;

    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }
        BookResDto bookResDto = bookService.책등록(bookSaveReqDto);

        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 저장 성공").body(bookResDto).build(), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList(){
        BookListResDto bookResDtoList = bookService.책목록();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 목록 보기 성공").body(bookResDtoList).build(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id){
        BookResDto bookResDto = bookService.책하나(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 한 건 보기 성공").body(bookResDto).build(), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 삭제 하기 성공").body(null).build(), HttpStatus.OK);
    }

    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }
        BookResDto bookResDto = bookService.책수정(id, bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 수정하기 성공").body(bookResDto).build(), HttpStatus.OK);
    }
}

