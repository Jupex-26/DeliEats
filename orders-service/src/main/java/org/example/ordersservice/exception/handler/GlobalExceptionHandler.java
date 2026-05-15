package org.example.ordersservice.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.example.ordersservice.exception.custom.*;
import org.example.ordersservice.exception.model.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> handleNotFoundException(NotFoundException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RepartidorExistsException.class)
    public ResponseEntity<CustomError> handleRepartidorExistsException(RepartidorExistsException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<CustomError> handleEmailExistsException(EmailExistsException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomError> handleBadRequestException(BadRequestException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarritoVacioException.class)
    public ResponseEntity<CustomError> handleCarritoVacio(CarritoVacioException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomError> handleConflictException(ConflictException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CustomError> handleUnauthorizedException(UnauthorizedException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> handleForbiddenException(ForbiddenException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        CustomError customError = CustomError.builder()
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(errors)
                .build();
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomError> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        CustomError customError = CustomError.builder()
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(errors)
                .build();
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        CustomError error = CustomError.builder()
                .message("Malformed JSON request: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomError> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getParameterName() + " parameter is missing: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String tipoEsperado = (ex.getRequiredType() != null)
                ? ex.getRequiredType().getSimpleName()
                : "un tipo diferente";

        String mensaje = String.format("El parámetro '%s' debe ser de tipo %s. Valor recibido: '%s'",
                ex.getName(), tipoEsperado, ex.getValue());

        CustomError error = CustomError.builder()
                .message(mensaje)
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        CustomError error = CustomError.builder()
                .message("HTTP method '" + ex.getMethod() + "' not supported for this endpoint. Supported methods: " + ex.getSupportedHttpMethods())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomError> handleEntityNotFoundException(EntityNotFoundException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleGenericException(Exception ex) {
        CustomError error = CustomError.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QuantityExceedsException.class)
    public ResponseEntity<CustomError> handleQuantityExceedsException(QuantityExceedsException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomError> handleIllegalArgumentException(IllegalArgumentException ex) {
        CustomError error = CustomError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
