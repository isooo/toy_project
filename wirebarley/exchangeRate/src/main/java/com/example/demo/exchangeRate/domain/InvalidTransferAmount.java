package com.example.demo.exchangeRate.domain;

public class InvalidTransferAmount extends RuntimeException {
    public static final String INVALID_TRANSFER_AMOUNT_MESSAGE = "송금액이 바르지 않습니다.";

    public InvalidTransferAmount() {
        super(INVALID_TRANSFER_AMOUNT_MESSAGE);
    }
}
