package com.openclassrooms.safetynet_alerts.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //400 paramètre invalide
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<String> handleBadRequestParams(Exception ex) {
        logger.error("Bad request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Bad request");
    }

    //404 aucune donnée trouvée
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException ex) {
        logger.error("No handler found: {}", ex.getMessage());
        return ResponseEntity.status(404).body("Not found");
    }

    // 500 erreurs techniques
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIoException(IOException exception) {
        logger.error("IO exception occurred", exception);
        return ResponseEntity.internalServerError()
                .body("Internal server error");
    }

    // 500 toutes les autres erreurs/exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception exception) {
        logger.error("Unexpected error occurred", exception);
        return ResponseEntity.internalServerError()
                .body("Internal server error");
    }
}

