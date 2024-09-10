package com.example.photocontest.advice;


import com.example.photocontest.exceptions.AuthorizationException;
import com.example.photocontest.exceptions.EntityDuplicateException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {

            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler(AuthorizationException.class)
    public Map<String, String> handleException(AuthorizationException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public Map<String, String> handleException(EntityDuplicateException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleException(EntityNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, String> handleException(UnauthorizedException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleException(IllegalArgumentException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleException(RuntimeException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error message ", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public String handleNotFound(Exception exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "redirect:/error";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleServerError(Throwable throwable, Model model) {
        model.addAttribute("message", throwable.getMessage());
        return "error";
    }

}
