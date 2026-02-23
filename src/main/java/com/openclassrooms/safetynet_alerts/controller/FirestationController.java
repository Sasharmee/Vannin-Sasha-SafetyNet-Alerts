package com.openclassrooms.safetynet_alerts.controller;


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

    /**
     * Contrôleur REST exposant l'endpoint /firestation permettant de gérer les associations entre adresses et numéros de caserne
     * Il permet de : récupérer les associations, en ajouter, mettre à jour des associations existantes et en supprimer
     *
     * @param firestationService service où se trouve la logique métier de l'endpoint CRUD
     */
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Récupère la liste des associations adresses/casernes
     *
     * @param firestation caserne à analyser
     * @return associations existantes entre la station et les adresses
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    @GetMapping
    public List<FirestationModel> getAllFirestation(FirestationModel firestation) throws IOException {

        logger.info("GET /firestation called");

        List<FirestationModel> result = firestationService.getAllFirestation(firestation);

        logger.info("GET /firestation success, {} firestations returned", result.size());

        return result;
    }

    /**
     * Ajoute une nouvelle association entre adresse et numéro de caserne
     *
     * @param firestation objet concernant l'adresse et le numéro de la station qu'on souhaite ajouter
     * @return liste mise à jour des associations
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //ADD
    @PostMapping
    public List<FirestationModel> addFirestation(@RequestBody FirestationModel firestation) throws IOException {
        logger.info("POST /firestation address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> result = firestationService.addFirestation(firestation);

        logger.info("POST /firestation success, total firestations={}", result.size());

        return result;
    }

    /**
     * Mise à jour d'une association existante
     *
     * @param firestation objet contenant l'association à modifier
     * @return la liste des associations mise à jour, ou null si aucune adresse n'est trouvée
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //UPDATE
    @PutMapping
    public List<FirestationModel> updateFirestation(@RequestBody FirestationModel firestation) throws IOException {
        logger.info("PUT /firestation address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> result = firestationService.updateFirestation(firestation);

        logger.info("PUT /firestation success");

        return result;
    }

    /**
     * Supprime une association
     * <p>
     * La suppression peut être effectuée selon l'adresse ou selon le numéro de caserne
     *
     * @param address adresse à supprimer
     * @param station numéro de caserne à supprimer
     * @return true si la suppression a eu lieu, false sinon
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //DELETE
    @DeleteMapping
    public boolean deleteFirestation(@RequestParam(required = false) String address, @RequestParam(required = false) String station) throws IOException {
        logger.info("DELETE /firestation address={} station={}", address, station);

        boolean removed = firestationService.deleteFirestation(address, station);

        logger.info("DELETE /firestation success removed={}", removed);

        return removed;
    }
}
