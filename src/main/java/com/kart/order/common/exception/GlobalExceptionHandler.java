package com.kart.order.common.exception;

import com.kart.order.catalog.exception.ProductNotFoundException;
import com.kart.order.common.dto.ExceptionResponse;
import com.kart.order.inventory.exception.InventoryAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleInventoryAlreadyExistsException(InventoryAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleProductNotFoundException(ProductNotFoundException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }
}
