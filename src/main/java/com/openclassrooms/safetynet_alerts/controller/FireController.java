package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.FireDTO;
import com.openclassrooms.safetynet_alerts.service.FireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FireController {

    private static final Logger logger = LoggerFactory.getLogger(FireController.class);


    private final FireService fireService;

    /**
     * Contrôleur REST exposant l'endpoint /fire.
     * Permet de récupérer la liste des habitants ainsi que le numéro de la caserne
     * correspondant à une adresse donnée.
     *
     * @param fireService service contenant la logique métier associée
     */


    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    /**
     * Récupère la liste des habitants et le numéro de la caserne correspondant
     * à l'adresse donnée.
     * Si aucune personne ne vit à cette adresse, un résultat nul est retourné.
     *
     * @param address adresse du foyer à analyser
     * @return un {@link FireDTO} contenant la liste des habitants et le numéro
     * de la caserne, ou null si aucune personne n'est présente
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */


    @GetMapping("/fire")
    public FireDTO getFireByAddress(@RequestParam String address) throws IOException {

        logger.info("GET /fire called with address={}", address);

        FireDTO result = fireService.getFireByAddress(address);

        logger.info("GET /fire success for address={}", address);


        return result;
    }
}
