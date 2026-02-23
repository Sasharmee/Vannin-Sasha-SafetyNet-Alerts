package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.service.PhoneAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);

    private final PhoneAlertService phoneAlertService;

    /**
     * Contrôleur REST exposant l'endpoint /phoneAlert avec comme paramètre firestation permettant de récupérer la liste de numéros de téléphones des habitants vivant aux adresses desservies par la caserne donnée
     *
     * @param phoneAlertService service où se retrouve la logique métier de l'endpoint
     */
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * Récupère la liste des numéros de téléphone des membres des foyers
     * desservis par la caserne donnée.
     *
     * @param stationNumber numéro de la caserne à analyser
     * @return une liste de numéros de téléphone. La liste est vide
     * si la station ne couvre aucun foyer.
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    @GetMapping
    public List<String> getPhoneByStation(@RequestParam("firestation") String stationNumber) throws IOException {

        logger.info("GET /phoneAlert firestation={}", stationNumber);

        List<String> phones = phoneAlertService.getPhoneByStation(stationNumber);

        logger.info("GET /phoneAlert success, {} phone numbers returned", phones.size());

        return phones;
    }
}
