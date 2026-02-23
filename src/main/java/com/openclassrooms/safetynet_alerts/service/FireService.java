package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.FireDTO;
import com.openclassrooms.safetynet_alerts.dto.ResidentInfoDTO;
import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsable de la logique métier de l'endpoint /fire.
 * <p>
 * Ce service permet d'identifier les résidents d'une adresse donnée et de trouver
 * le numéro de la caserne qui couvre cette adresse.
 */
@Service
public class FireService {

    private static final Logger logger = LoggerFactory.getLogger(FireService.class);


    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalrecordRepository medicalrecordRepository;
    private final AgeService ageService;

    /**
     * Construit le service FireService
     *
     * @param personRepository        repository permettant l'accès aux données des personnes
     * @param firestationRepository   repository permettant l'accès aux données des casernes
     * @param medicalrecordRepository repository permettant l'accès aux données médicales des personnes
     * @param ageService              service permettant le calcul de l'âge et de déterminer qui est majeur/mineur
     */
    public FireService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalrecordRepository medicalrecordRepository, AgeService ageService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.ageService = ageService;
    }

    /**
     * Récupère la liste des résidents et leurs informations ainsi que le numéro de caserne pour une adresse donnée
     *
     * @param address adresse à analyser
     * @return un {@link FireDTO} contenant la liste des résidents et le numéro de caserne
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    public FireDTO getFireByAddress(String address) throws IOException {

        logger.debug("Starting fire search for address={}", address);


        List<FirestationModel> firestations = firestationRepository.findAll();

        String stationNumber = null;

        for (FirestationModel firestation : firestations) {
            if (firestation.getAddress().equals(address)) {
                stationNumber = firestation.getStation();
            }
        }

        logger.debug("Station found for address={} is stationNumber={}", address, stationNumber);


        List<PersonModel> persons = personRepository.findAll();
        List<PersonModel> household = new ArrayList<>();

        for (PersonModel person : persons) {
            if (person.getAddress().equalsIgnoreCase(address)) {
                household.add(person);
            }
        }

        logger.debug("Household size for address={} is {}", address, household.size());


        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();
        logger.debug("Loaded {} medicalrecords", medicalrecords.size());


        List<ResidentInfoDTO> residents = new ArrayList<>();

        for (PersonModel person : household) {
            int age = ageService.calculateAge(person);

            MedicalrecordModel foundMedical = null;
            for (MedicalrecordModel mr : medicalrecords) {
                boolean samePerson = mr.getFirstName().equalsIgnoreCase(person.getFirstName()) && mr.getLastName().equalsIgnoreCase(person.getLastName());

                if (samePerson) {
                    foundMedical = mr;
                }
            }

            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (foundMedical != null) {
                if (foundMedical.getMedications() != null) medications = foundMedical.getMedications();
                if (foundMedical.getAllergies() != null) allergies = foundMedical.getAllergies();
            }

            ResidentInfoDTO residentDTO = new ResidentInfoDTO(person.getLastName(), person.getPhone(), age, medications, allergies);
            residents.add(residentDTO);
        }
        logger.debug("Fire search completed for address={}, residentsReturned={}, stationNumber={}",
                address, residents.size(), stationNumber);


        return new FireDTO(residents, stationNumber);
    }
}
