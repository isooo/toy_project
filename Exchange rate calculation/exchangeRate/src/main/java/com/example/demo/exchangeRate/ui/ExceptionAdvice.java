package com.example.demo.exchangeRate.ui;

import com.example.demo.exchangeRate.domain.InvalidTransferAmount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    private static final String INVALID_REQUEST = "유효하지 않은 요청입니다.";

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(INVALID_REQUEST)
                ;
    }

    @ExceptionHandler(value = InvalidTransferAmount.class)
    public ResponseEntity<String> invalidTransferAmount(final InvalidTransferAmount exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(exception.getMessage())
                ;
    }
}
