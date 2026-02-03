package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.service.FirestationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService){
        this.firestationService=firestationService;
    }

    //ADD
    @PostMapping
    public List<FirestationModel> addFirestation(@RequestBody FirestationModel firestation) throws IOException{
        return firestationService.addFirestation(firestation);
    }

    //UPDATE
    @PutMapping
    public List<FirestationModel> updateFirestation(@RequestBody FirestationModel firestation) throws IOException{
        return firestationService.updateFirestation(firestation);
    }

    //DELETE
    @DeleteMapping
    public boolean deleteFirestation( @RequestParam(required = false) String address, @RequestParam(required = false) String station) throws IOException{
        return firestationService.deleteFirestation(address, station);
    }
}
