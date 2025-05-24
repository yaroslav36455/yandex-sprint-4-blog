package by.tyv.controller;

import by.tyv.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ServiceControllerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleException(DataNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
