package com.shhwang0930.beproject.exception;

import com.shhwang0930.beproject.dto.responseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CpuExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<responseDTO<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        responseDTO<String> response = new responseDTO<String>(HttpStatus.BAD_REQUEST.value(), "잘못된 시간 입력");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<responseDTO<String>> handleRuntimeException(RuntimeException ex) {
        responseDTO<String> response = new responseDTO<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DB 에러");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
