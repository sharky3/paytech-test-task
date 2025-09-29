package com.magdieva.testtask.exception;

public class PaymentProcessingException extends RuntimeException {

    public PaymentProcessingException(String errorCode, String message) {
        super("Payment failed with " + errorCode + " error code: " + message);
    }
}
