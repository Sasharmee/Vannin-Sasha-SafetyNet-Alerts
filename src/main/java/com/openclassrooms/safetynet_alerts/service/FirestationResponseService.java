package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.dto.PersonFirestationDTO;
import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsable de la logique métier de l'endpoint /firestation (paramètre stationNumber).
 * <p>
 * Ce service permet d'identifier les personnes couvertes par une caserne
 * et de compter le nombre d'adultes et d'enfants.
 */

@Service
public class FirestationResponseService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationResponseService.class);


    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final AgeService ageService;

    /**
     * Construit le service FirestationResponse
     *
     * @param firestationRepository repository permettant d'accéder aux données des casernes
     * @param personRepository      repository permettant d'accéder aux données des personnes
     * @param ageService            service permettant de calculer l'âge des personnes et déterminer si elles sont majeures ou non
     */
    public FirestationResponseService(FirestationRepository firestationRepository, PersonRepository personRepository, AgeService ageService) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.ageService = ageService;
    }

    /**
     * Récupère la liste des personnes couvertes par une caserne donnée ainsi que le nombre d'enfants et d'adultes
     *
     * @param stationNumber numéro de la caserne à analyser
     * @return un {@link FirestationResponseDTO} contenant la liste des personnes couvertes, le nombre d'adultes et d'enfants
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    public FirestationResponseDTO getPersonsCoveredByStation(String stationNumber) throws IOException {

        logger.debug("Starting firestation response search for stationNumber={}", stationNumber);


        List<FirestationModel> firestations = firestationRepository.findAll();
        List<String> addresses = new ArrayList<>();
        for (FirestationModel f : firestations) {
            if (f.getStation().equals(stationNumber)) {
                addresses.add(f.getAddress());
            }
        }

        logger.debug("Found {} addresses covered by station {}", addresses.size(), stationNumber);


        List<PersonModel> persons = personRepository.findAll();
        List<PersonFirestationDTO> personFirestationDTOS = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (PersonModel person : persons) {
            if (addresses.contains(person.getAddress())) {
                personFirestationDTOS.add(new PersonFirestationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()));


                if (ageService.isAdult(person)) {
                    adultCount++;
                } else {
                    childCount++;
                }
            }
        }
        logger.debug("Firestation response completed for station {}, adults={}, children={}",
                stationNumber, adultCount, childCount);


        return new FirestationResponseDTO(personFirestationDTOS, adultCount, childCount);

    }
}
