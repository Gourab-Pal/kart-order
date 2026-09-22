package com.kart.order.common.exception;

import com.kart.order.cart.exception.CartItemNotFoundException;
import com.kart.order.cart.exception.CartNotFoundException;
import com.kart.order.cart.exception.IllegalCartStateException;
import com.kart.order.catalog.exception.ProductNotFoundException;
import com.kart.order.checkout.exception.CheckoutException;
import com.kart.order.common.dto.ExceptionResponse;
import com.kart.order.inventory.exception.InventoryAlreadyExistsException;
import com.kart.order.inventory.exception.InventoryNotFoundException;
import com.kart.order.order.exception.IllegalOrderStateException;
import com.kart.order.order.exception.OrderException;
import com.kart.order.order.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

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
        FieldError fieldError = exception.getBindingResult().getFieldErrors().get(0);
        return new ExceptionResponse(fieldError.getDefaultMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ExceptionResponse handleResourceAccessException(ResourceAccessException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse("Service unavailable: " + exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleInventoryNotFoundException(InventoryNotFoundException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleCartNotFoundException(CartNotFoundException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(IllegalCartStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleIllegalCartStateException(IllegalCartStateException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleCartItemNotFoundException(CartItemNotFoundException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(CheckoutException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleCheckoutException(CheckoutException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleOrderNotFoundException(OrderNotFoundException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(IllegalOrderStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleIllegalOrderStateException(IllegalOrderStateException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }

    @ExceptionHandler(OrderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleOrderException(OrderException exception) {
        log.error(exception.getMessage());
        return new ExceptionResponse(exception.getMessage(), OffsetDateTime.now());
    }
}
