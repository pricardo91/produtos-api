package com.mixfiscal.produtos_api.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorApiResponse> handlerApiException(ApiException e, HttpServletRequest request) {

        log.error("[erro] - {} - {}", e.getClass().getSimpleName(), e.getMessage());

        ErrorApiResponse errorApiResponse = ErrorApiResponse.builder()
                .status(e.getHttpStatus().value())
                .message(e.getMessage())
                .description(e.getDescription())
                .error(e.getHttpStatus().getReasonPhrase())
                .path(request.getMethod() + ": " + request.getRequestURI())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(errorApiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {

        log.error("[erro] - MethodArgumentNotValidException - {}", e.getMessage());

        List<String> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ErrorApiResponse errorApiResponse = ErrorApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Erro de validação de campos")
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(erros)
                .path(request.getMethod() + ": " + request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(errorApiResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorApiResponse> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {

        log.error("[erro] - DataIntegrityViolationException - {}", e.getMessage());

        ErrorApiResponse errorApiResponse = ErrorApiResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message("Violação de dados únicos no banco")
                .path(request.getMethod() + ": " + request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorApiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleGenericException(Exception ex, HttpServletRequest request) {

        log.error("Erro não tratado: {} - Path: {}", ex.getMessage(), request.getRequestURI(), ex);

        ErrorApiResponse errorResponse = ErrorApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Erro interno do servidor")
                .description("Entre em contato com o suporte se o problema persistir")
                .path(request.getMethod() + ": " + request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
