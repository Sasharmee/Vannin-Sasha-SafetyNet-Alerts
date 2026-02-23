package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.service.FirestationResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/firestation")
public class FirestationResponseController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationResponseController.class);


    private final FirestationResponseService firestationResponseService;

    /**
     * Contrôleur REST exposant l'endpoint /firestation avec le paramètre stationNumber retournant la liste de personnes couvertes ainsi que le décompte des adultes et enfants par la caserne correspondante
     *
     * @param firestationResponseService service contenant la logique métier de l'endpoint
     */
    public FirestationResponseController(FirestationResponseService firestationResponseService) {
        this.firestationResponseService = firestationResponseService;
    }

    /**
     * Récupère la liste des personnes couvertes par la caserne ainsi que le décompte des adultes et enfants.
     *
     * @param stationNumber numéro de la caserne couvrant les foyers à analyser
     * @return FirestationResponseDTO contenant la liste des personnes ainsi que les décomptes, ou null si la station ne couvre personne
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //GET
    @GetMapping(params = "stationNumber")
    public FirestationResponseDTO getPersonsByFirestation(@RequestParam String stationNumber) throws IOException {

        logger.info("GET /firestation stationNumber={}", stationNumber);

        FirestationResponseDTO result =
                firestationResponseService.getPersonsCoveredByStation(stationNumber);

        logger.info("GET /firestation success for stationNumber={}", stationNumber);

        return result;
    }
}

