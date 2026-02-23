package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalrecordController.class);

    private final MedicalrecordService medicalrecordService;

    /**
     * Contrôleur REST exposant l'endpoint /medicalRecord permettant l'association entre personnes et leurs données médicales
     * Il permet de récupérer les associations, d'en ajouter, d'en supprimer et modifier les données médicales des personnes existantes
     *
     * @param medicalrecordService service où se trouve la logique métier de l'endpoint CRUD
     */
    public MedicalrecordController(MedicalrecordService medicalrecordService) {
        this.medicalrecordService = medicalrecordService;
    }

    /**
     * Récupère la liste des associations entre personnes et données médicales
     *
     * @return la liste des associations
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    @GetMapping
    public List<MedicalrecordModel> getAllMedicalrecord() throws IOException {

        logger.info("GET /medicalRecord called");

        List<MedicalrecordModel> result = medicalrecordService.getAllMedicalrecord();

        logger.info("GET /medicalRecord success, {} medicalrecords returned", result.size());

        return result;
    }

    /**
     * Ajoute une nouvelle association
     *
     * @param medicalrecord objet concernant la personne et les données médicales qu'on souhaite ajouter
     * @return le dossier médical créé
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //POST
    @PostMapping
    public MedicalrecordModel addMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException {

        logger.info("POST /medicalRecord firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        MedicalrecordModel result = medicalrecordService.addMedicalrecord(medicalrecord);

        logger.info("POST /medicalRecord success for firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return result;
    }

    /**
     * Mise à jour d'une association existante
     *
     * @param medicalrecord objet concernant l'association à modifier
     * @return le dossier médical mis à jour, ou null si inexistant
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //PUT
    @PutMapping
    public MedicalrecordModel updateMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException {
        logger.info("PUT /medicalRecord firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        MedicalrecordModel result = medicalrecordService.updateMedicalrecord(medicalrecord);

        logger.info("PUT /medicalRecord success for firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return result;
    }

    /**
     * Suppression d'une association
     * La suppression nécessite obligatoirement le prénom ET le nom.
     *
     * @param firstName prénom à renseigner pour supprimer
     * @param lastName  nom à renseigner pour supprimer
     * @return true si la suppression a eu lieu, sinon false
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //DELETE
    @DeleteMapping
    public boolean deleteMedicalrecord(@RequestParam String firstName,
                                       @RequestParam String lastName) throws IOException {
        logger.info("DELETE /medicalRecord firstName={} lastName={}", firstName, lastName);

        boolean removed = medicalrecordService.deleteMedicalrecord(firstName, lastName);

        logger.info("DELETE /medicalRecord success removed={}", removed);

        return removed;
    }

}
