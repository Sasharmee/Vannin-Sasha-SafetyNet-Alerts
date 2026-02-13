package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
import com.openclassrooms.safetynet_alerts.service.FloodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping
public class FloodController {

    private static final Logger logger = LoggerFactory.getLogger(FloodController.class);

    private final FloodService floodService;

    public FloodController(FloodService floodService){
        this.floodService = floodService;
    }
@GetMapping("/flood/stations")
    public List<FloodDTO> getFloodByStation(@RequestParam String stations) throws IOException{

        logger.info("GET /flood/stations={} stations number", stations);

        List<FloodDTO> result = floodService.getFloodByStation(stations);

        logger.info("GET /flood/stations={}, success, {} flood returned", stations, result.size());

        return result;
    }
}
