package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/firestation") //mapping?
public class FirestationController {

    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);


    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService){
        this.firestationService=firestationService;
    }

    //GET
    @GetMapping
    public List<FirestationModel> getAllFirestation(FirestationModel firestation) throws IOException{

        logger.info("GET /firestation called");

        List<FirestationModel> result = firestationService.getAllFirestation(firestation);

        logger.info("GET /firestation success, {} firestations returned", result.size());

        return result;
    }

    //ADD
    @PostMapping
    public List<FirestationModel> addFirestation(@RequestBody FirestationModel firestation) throws IOException{
        logger.info("POST /firestation address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> result = firestationService.addFirestation(firestation);

        logger.info("POST /firestation success, total firestations={}", result.size());

        return result;
    }

    //UPDATE
    @PutMapping
    public List<FirestationModel> updateFirestation(@RequestBody FirestationModel firestation) throws IOException{
        logger.info("PUT /firestation address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> result = firestationService.updateFirestation(firestation);

        logger.info("PUT /firestation success");

        return result;
    }

    //DELETE
    @DeleteMapping
    public boolean deleteFirestation( @RequestParam(required = false) String address, @RequestParam(required = false) String station) throws IOException{
        logger.info("DELETE /firestation address={} station={}", address, station);

        boolean removed = firestationService.deleteFirestation(address, station);

        logger.info("DELETE /firestation success removed={}", removed);

        return removed;
    }
}
