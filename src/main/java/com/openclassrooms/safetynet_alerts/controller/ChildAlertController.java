package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.service.ChildAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService){
        this.childAlertService = childAlertService;
    }

    //GET
    @GetMapping
    public Object getChildrenByAddress(@RequestParam String address) throws IOException {

        logger.info("GET /childAlert address={}", address);

        // On appelle le service
        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress(address);

        // Si pas d'enfant, on renvoie une chaîne vide (comme demandé)
        if (result.isEmpty()) {
            logger.info("GET /childAlert success, 0 children returned (empty string response)");
            return "";
        }

        // Sinon on renvoie la liste des enfants (JSON)
        logger.info("GET /childAlert success, {} children returned", result.size());
        return result;
    }
}
