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

    /**
     * Contrôleur REST exposant l'endpoint /childAlert permettant de récupérer les enfants ainsi que qu'une liste des autres membres du foyer vivant à une adresse donnée
     *
     * @param childAlertService service où se retrouve la logique métier permettant de récupérer les enfants selon l'adresse donnée ainsi qu'une liste des autres membres du foyer
     */

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    /**
     * Récupère la liste des enfants (=<18 ans) ainsi que qu'une liste des autres membres du foyer vivant à l'adresse donnée.
     * Si aucun enfant, une liste vide est retournée.
     *
     * @param address adresse du foyer à analyser
     * @return une liste de ChildAlertDTO ou une chaine vide lorsqu'aucun enfant n'est présent à l'adresse
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    @GetMapping
    public Object getChildrenByAddress(@RequestParam String address) throws IOException {

        logger.info("GET /childAlert address={}", address);

        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress(address);

        if (result.isEmpty()) {
            logger.info("GET /childAlert success, 0 children returned (empty string response)");
            return "";
        }

        logger.info("GET /childAlert success, {} children returned", result.size());
        return result;
    }
}
