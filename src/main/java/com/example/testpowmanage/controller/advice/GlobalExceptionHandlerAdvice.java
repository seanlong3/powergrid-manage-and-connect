package com.example.testpowmanage.controller.advice;

import com.example.testpowmanage.common.ResponseResultVO;
import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PSQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandlerAdvice {
    @ExceptionHandler(value = PSQLException.class)
    public ResponseEntity<ResponseResultVO<String>> insertExceptionHandler(Exception e){
        log.info(e.getMessage());
        return new ResponseEntity<>(new ResponseResultVO<>(
                400,
                "SQL error",
                "null"), HttpStatus.OK);
    }
}