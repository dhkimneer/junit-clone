package com.meta.junitproject.web.handler;

import com.meta.junitproject.web.dto.response.CommonRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e) {
        return new ResponseEntity<>(CommonRespDto.builder().code(-1).message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
