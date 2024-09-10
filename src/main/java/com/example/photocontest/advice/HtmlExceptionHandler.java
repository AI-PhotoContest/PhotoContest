package com.example.photocontest.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class HtmlExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public String handleNotFound(Exception exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error"; // Пренасочване към /error контролера
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleServerError(Throwable throwable, Model model) {
        model.addAttribute("message", throwable.getMessage());
        return "error"; // Връща страницата error.html
    }
}