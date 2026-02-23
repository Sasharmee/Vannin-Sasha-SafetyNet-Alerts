package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service responsable du calcul de l'âge d'une personne à partir de sa date de naissance enregistrée dans les données médicales
 */
@Service
public class AgeService {

    private static final Logger logger = LoggerFactory.getLogger(AgeService.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final int ADULT_AGE_THRESHOLD = 18;

    private final MedicalrecordRepository medicalrecordRepository;
    private final Clock clock;

    /**
     * Construit le service de calcul de l'âge
     *
     * @param medicalrecordRepository repository permettant d'accéder aux données médicales
     * @param clock                   horloge injectée permettant de calculer l'âge de manière fiable et testable
     */
    public AgeService(MedicalrecordRepository medicalrecordRepository, Clock clock) {
        this.medicalrecordRepository = medicalrecordRepository;
        this.clock = clock;
    }

    /**
     * Calcule l'âge d'une personne à partir de sa date de naissance enregistrée dans les données médicales
     *
     * <p>
     * La date de naissance est convertie en {@link LocalDate} puis comparée
     * à la date actuelle obtenue via le {@link Clock} injecté.
     * </p>
     *
     * @param person personne dont l'âge doit être calculé
     * @return âge en années, ou -1 si aucun dossier médical correspondant est retrouvé
     * @throws IOException en cas d'erreur lors de l'accès aux données médicales
     */
    public int calculateAge(PersonModel person) throws IOException {

        logger.debug("Calculating age for {} {}", person.getFirstName(), person.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for (MedicalrecordModel medicalrecord : medicalrecords) {
            if (medicalrecord.getFirstName().equalsIgnoreCase(person.getFirstName()) && medicalrecord.getLastName().equalsIgnoreCase(person.getLastName())) {

                LocalDate birthdate = LocalDate.parse(medicalrecord.getBirthdate(), DATE_TIME_FORMATTER);
                LocalDate today = LocalDate.now(clock);

                int age = Period.between(birthdate, today).getYears();
                logger.debug("Age calculated for {} {} = {}", person.getFirstName(), person.getLastName(), age);

                return age;
            }
        }
        logger.debug("No medical record found for {} {}", person.getFirstName(), person.getLastName());
        return -1;

    }

    /**
     * Détermine si la personne est adulte, c'est-à-dire si son âge est strictement supérieur à 18 ans
     *
     * @param person personne à analyser
     * @return {@code true} si la personne est adulte, sinon {@code false}
     * @throws IOException en cas d'erreur lors du calcul de l'âge
     */
    public boolean isAdult(PersonModel person) throws IOException {
        boolean isAdult = this.calculateAge(person) > ADULT_AGE_THRESHOLD;
        logger.debug("{} {} isAdult={}", person.getFirstName(), person.getLastName(), isAdult);
        return isAdult;
    }

    /**
     * Détermine si la personne est mineure, c'est-à-dire si son âge est inférieur ou égal à 18 ans
     *
     * @param person personne à analyser
     * @return {@code true} si la personne est mineure, sinon {@code false}
     * @throws IOException en cas d'erreur lors du calcul de l'âge
     */
    public boolean isChild(PersonModel person) throws IOException {
        boolean isChild = this.calculateAge(person) <= ADULT_AGE_THRESHOLD;
        logger.debug("{} {} isChild={}", person.getFirstName(), person.getLastName(), isChild);
        return isChild;
    }

}
