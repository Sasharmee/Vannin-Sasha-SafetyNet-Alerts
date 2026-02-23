package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Service responsable de la logique métier liée à l'endpoint CRUD /medicalRecord.
 * <p>
 * Ce service permet de récupérer, ajouter, mettre à jour et supprimer
 * les données médicales associées aux personnes.
 */

@Service
public class MedicalrecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalrecordService.class);

    private final PersonRepository personRepository;
    private MedicalrecordRepository medicalrecordRepository;

    /**
     * Construit le service MedicalRecord
     *
     * @param medicalrecordRepository repository permettant d'accéder aux données médicales des personnes
     * @param personRepository        repository permettant d'accéder aux données personnelles des personnes
     */

    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository, PersonRepository personRepository) {
        this.medicalrecordRepository = medicalrecordRepository;
        this.personRepository = personRepository;
    }

    /**
     * Récupère la liste complète des personnes et de leurs données médicales
     *
     * @return liste de tous les dossiers médicaux
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    public List<MedicalrecordModel> getAllMedicalrecord() throws IOException {
        logger.debug("Fetching all medicalrecords");

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        logger.debug("Found {} medicalrecords", medicalrecords.size());

        return medicalrecords;
    }

    /**
     * Ajout d'un nouveau dossier médical
     *
     * @param medicalrecord objet contenant la personne et ses données médicales
     * @return dossier médical créé
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //ADD
    public MedicalrecordModel addMedicalrecord(MedicalrecordModel medicalrecord) throws IOException {

        logger.debug("Adding medicalrecord for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean exists = medicalrecords.stream().anyMatch(m -> m.getFirstName().equals(medicalrecord.getFirstName()) && m.getLastName().equals(medicalrecord.getLastName()));
        if (exists) {
            logger.debug("Medicalrecord already exists for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
            throw new IllegalArgumentException("Medicalrecord already exists");
        }
        medicalrecords.add(medicalrecord);
        medicalrecordRepository.saveAll(medicalrecords);

        logger.debug("Medicalrecord added successfully for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return medicalrecord;
    }

    /**
     * Met à jour un dossier médical existant
     *
     * @param medicalrecord objet contenant les nouvelles données médicales
     * @return le dossier médical mis à jour ou {@code null} si aucun dossier médical correspondant n'est retrouvé
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //UPDATE
    public MedicalrecordModel updateMedicalrecord(MedicalrecordModel medicalrecord) throws IOException {

        logger.debug("Updating medicalrecord for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for (MedicalrecordModel m : medicalrecords) {
            if (m.getFirstName().equals(medicalrecord.getFirstName()) && m.getLastName().equals(medicalrecord.getLastName())) {
                m.setBirthdate(medicalrecord.getBirthdate());
                m.setMedications(medicalrecord.getMedications());
                m.setAllergies(medicalrecord.getAllergies());
                medicalrecordRepository.saveAll(medicalrecords);

                logger.debug("Medicalrecord updated successfully for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

                return m;
            }
        }
        logger.debug("No medicalrecord found to update for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
        return null;
    }

    /**
     * Supprime un dossier médical existant, nécessite la combinaison du nom et du prénom
     *
     * @param firstName prénom de la personne
     * @param lastName  nom de la personne
     * @return {@code true} si la suppression a eu lieu, sinon {@code false}
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //DELETE
    public boolean deleteMedicalrecord(String firstName, String lastName) throws IOException {

        logger.debug("Deleting medicalrecord for {} {}", firstName, lastName);


        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean removed = medicalrecords.removeIf(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName));

        if (removed) {
            medicalrecordRepository.saveAll(medicalrecords);
            logger.debug("Medicalrecord deleted successfully for {} {}", firstName, lastName);
        } else {
            logger.debug("No medicalrecord found to delete for {} {}", firstName, lastName);
        }
        return removed;
    }

}
