package com.spring.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionController {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class.getName());

    /**
     * Handles invalid requests entities/models, thrown by javax validation e.g. a NotEmpty property is empty
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ObjectError>> handleValidationException(HttpServletRequest request,
                                                                       MethodArgumentNotValidException ex) {
        String requestPath = request.getRequestURI();
        logger.warn("Canceled request to {} due to invalid request parameters", requestPath);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getBindingResult().getAllErrors());
    }

    /**
     * Handles all other errors, so that they aren't leaked to the client
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handleUncatched(HttpServletRequest request, Exception ex) {
        String requestPath = request.getRequestURI();
        logger.error("Request to {} through uncaught exception", requestPath, ex);
    }
}
