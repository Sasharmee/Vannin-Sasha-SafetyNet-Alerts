package com.openclassrooms.safetynet_alerts.service;

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
 * Service responsable de la logique métier de l'endpoint /phoneAlert.
 * <p>
 * Ce service permet de récupérer les numéros de téléphone des résidents
 * desservis par la caserne donnée.
 */

@Service
public class PhoneAlertService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertService.class);

    //injection des deux repository car on va croiser les données
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    /**
     * Construit le service PhoneAlert
     *
     * @param personRepository      repository permettant d'accéder aux données personnelles des personnes
     * @param firestationRepository repository permettant d'accéder aux données des casernes
     */
    public PhoneAlertService(PersonRepository personRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    /**
     * Récupère la liste des numéros de téléphone des résidents desservis par la caserne donnée
     *
     * @param stationNumber numéro de la caserne à analyser
     * @return liste de numéro de téléphone des résidents couverts
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    public List<String> getPhoneByStation(String stationNumber) throws IOException {

        logger.debug("Starting phone alert search for stationNumber={}", stationNumber);

        List<FirestationModel> firestations = firestationRepository.findAll();
        logger.debug("Loaded {} firestations", firestations.size());

        List<String> addresses = new ArrayList<>();

        for (FirestationModel fs : firestations) {
            if (fs.getAddress() != null && fs.getStation().equals(stationNumber)) {
                addresses.add(fs.getAddress());
            }
        }
        logger.debug("Found {} addresses covered by stationNumber={}", addresses.size(), stationNumber);

        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons", persons.size());

        List<String> phones = new ArrayList<>();

        for (PersonModel p : persons) {
            if (p.getAddress() != null && addresses.contains(p.getAddress())) {
                //on ajoute son numéro
                if (p.getPhone() != null) {
                    phones.add(p.getPhone());
                }
            }
        }
        logger.debug("Phone alert search completed for stationNumber={}, phonesReturned={}",
                stationNumber, phones.size());

        return phones;
    }
}
