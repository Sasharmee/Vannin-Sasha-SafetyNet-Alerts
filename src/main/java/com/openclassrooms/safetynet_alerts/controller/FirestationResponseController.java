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

    public FirestationResponseController(FirestationResponseService firestationResponseService){
        this.firestationResponseService = firestationResponseService;
    }

    //GET - persons by station
    @GetMapping(params = "stationNumber")
    public FirestationResponseDTO getPersonsByFirestation(@RequestParam String stationNumber) throws IOException {

        logger.info("GET /firestation stationNumber={}", stationNumber);

        FirestationResponseDTO result =
                firestationResponseService.getPersonsCoveredByStation(stationNumber);

        logger.info("GET /firestation success for stationNumber={}", stationNumber);

        return result;
    }
}

