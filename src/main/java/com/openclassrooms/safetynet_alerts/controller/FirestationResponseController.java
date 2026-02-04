package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.service.FirestationResponseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/firestation")
public class FirestationResponseController {

    private final FirestationResponseService firestationResponseService;

    public FirestationResponseController(FirestationResponseService firestationResponseService){
        this.firestationResponseService = firestationResponseService;
    }

    //GET - persons by station
    @GetMapping(params = "stationNumber")
    public FirestationResponseDTO getPersonsByFirestation(@RequestParam String stationNumber) throws IOException {
        return firestationResponseService.getPersonsCoveredByStation(stationNumber); // méthode à mettre en place dans service
    }
}

// attention marche avec ce lien http://localhost:8080/firestation?stationNumber=3
