package com.unity.back.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error", "VALIDATION_ERROR");
        resp.put("message", ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : "参数校验失败");
        return resp;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error", "BAD_REQUEST");
        resp.put("message", ex.getMessage());
        return resp;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error", "INTERNAL_SERVER_ERROR");
        resp.put("message", ex.getMessage());
        return resp;
    }
}
