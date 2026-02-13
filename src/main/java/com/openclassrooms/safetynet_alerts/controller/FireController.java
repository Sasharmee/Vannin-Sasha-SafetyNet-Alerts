package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.FireDTO;
import com.openclassrooms.safetynet_alerts.service.FireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);


    private final FireService fireService;

    public FireController(FireService fireService){
        this.fireService = fireService;
    }

    @GetMapping("/fire")
    public FireDTO getFireByAddress(String address) throws IOException{

        logger.info("GET /fire called with address={}", address);

        FireDTO result = fireService.getFireByAddress(address);

        logger.info("GET /fire success for address={}", address);


        return  result;
    }
}
