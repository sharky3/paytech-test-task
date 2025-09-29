package com.magdieva.testtask.exception;

import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException {

    public IntegrationException(String errorCode, String message) {
        super(errorCode + ": " + message);
    }
}
