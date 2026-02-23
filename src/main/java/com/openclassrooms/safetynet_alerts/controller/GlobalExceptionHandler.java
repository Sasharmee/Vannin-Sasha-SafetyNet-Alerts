package com.openclassrooms.safetynet_alerts.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

/**
 * Gestionnaire global des exceptions de l'application.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les erreurs liées aux paramètres de requête invalides ou manquants
     *
     * @param ex exception déclenchée lors de la validation des paramètres
     * @return réponse HTTP 400 (Bad Request)
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<String> handleBadRequestParams(Exception ex) {
        logger.error("Bad Request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Bad Request");
    }

    /**
     * Gère les requêtes envoyées vers un endpoint inexistant
     *
     * @param ex exception déclenchée lorsque aucun handler ne correspond à la requête
     * @return réponse HTTP 404 (Not Found)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException ex) {
        logger.error("No handler Found: {}", ex.getMessage());
        return ResponseEntity.status(404).body("Not Found");
    }

    /**
     * Gère les erreurs techniques liées aux opérations d'entrée/sortie
     *
     * @param exception exception IO levée lors de l'accès aux données
     * @return réponse HTTP 500 (Internal server error)
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIoException(IOException exception) {
        logger.error("IO exception occurred", exception);
        return ResponseEntity.internalServerError()
                .body("Internal server error");
    }

    /**
     * Gère toutes les exceptions non spécifiquement interceptées
     *
     * @param exception exception inattendue
     * @return réponse HTTP 500 (Internal server error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception exception) {
        logger.error("Unexpected error occurred", exception);
        return ResponseEntity.internalServerError()
                .body("Internal server error");
    }
}

