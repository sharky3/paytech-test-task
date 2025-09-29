package com.magdieva.testtask.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@ControllerAdvice
class ControllerExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView errorHandler(Exception e) {
        var logRef = UUID.randomUUID();
        log.error("Error occurred, logRef={}: {}", logRef, e.getMessage(), e);
        var modelAndView = new ModelAndView();
        var errorMessage = e.getMessage() != null ? e.getMessage() : "Internal Error";
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("logRef", logRef);
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }
}