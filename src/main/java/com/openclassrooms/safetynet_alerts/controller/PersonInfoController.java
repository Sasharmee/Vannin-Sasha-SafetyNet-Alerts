package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynet_alerts.service.PersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    private final PersonInfoService personInfoService;

    /**
     * Contrôleur REST exposant l'endpoint /personInfoLastName avec comme paramètre lastName permettant de récupérer les données personnelles et médicales d'une personne
     *
     * @param personInfoService service où se trouve la logique métier de l'endpoint
     */

    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    /**
     * Récupère la liste des informations personnelles et médicales
     * des personnes portant le nom de famille donné.
     *
     * @param lastName nom de famille à analyser
     * @return une liste de {@link PersonInfoDTO}. La liste est vide
     * si aucune personne ne correspond.
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    @GetMapping("/personInfolastName={lastName}")
    public List<PersonInfoDTO> getPersonInfoByLastName(@PathVariable String lastName) throws IOException {

        logger.info("GET /personInfolastName={} called", lastName);

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName(lastName);

        logger.info("GET /personInfolastName={} success, {} results returned", lastName, result.size());

        return result;
    }
}
