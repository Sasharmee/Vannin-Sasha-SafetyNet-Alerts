package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    /**
     * Contrôleur REST exposant l'endpoint /communityEmail permettant de récupérer les adresses mail de tous les habitants de la ville
     *
     * @param communityEmailService service où se retrouve la logique métier permettant de récupérer les mails par ville
     */
    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * Récupère la liste des adresses mail des habitants de la ville donnée
     * Si aucune personne n'y vit, on renvoie une liste vide
     *
     * @param city ville où vivent les habitants dont on souhaite récupérer les mails
     * @return une liste de mails ou une liste vide si personne ne vit dans la ville donnée
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    @GetMapping("/communityEmail")
    public List<String> getEmailsByCity(@RequestParam String city) throws IOException {

        logger.info("GET /communityEmail city={}", city);

        List<String> emails = communityEmailService.getEmailsByCity(city);

        logger.info("GET /communityEmail success, {} emails returned", emails.size());

        return emails;
    }
}
