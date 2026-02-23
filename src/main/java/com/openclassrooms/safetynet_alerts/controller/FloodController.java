package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
import com.openclassrooms.safetynet_alerts.service.FloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    private final FloodService floodService;

    /**
     * Contrôleur REST exposant l'endpoint /flood/stations.
     * Permet de récupérer la liste des foyers couverts par une ou plusieurs casernes.
     *
     * @param floodService service contenant la logique métier associée
     */

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    /**
     * Récupère la liste des foyers couverts par les casernes indiquées.
     * Les résidents sont regroupés par adresse.
     *
     * @param stations liste des numéros de caserne à analyser
     * @return une liste de {@link FloodDTO} contenant les adresses et les résidents associés
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */


    @GetMapping("/flood/stations")
    public List<FloodDTO> getFloodByStation(@RequestParam String stations) throws IOException {

        logger.info("GET /flood/stations={} stations number", stations);

        List<FloodDTO> result = floodService.getFloodByStation(stations);

        logger.info("GET /flood/stations={}, success, {} households returned", stations, result.size());

        return result;
    }
}
