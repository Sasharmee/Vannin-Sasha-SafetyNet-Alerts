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

@Service
public class FirestationResponseService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationResponseService.class);


    //chercher ce que c'est ça et comment le traduire ?
    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final AgeService ageService;

    public FirestationResponseService(FirestationRepository firestationRepository, PersonRepository personRepository, AgeService ageService) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.ageService = ageService;
    }
    //GET - persons covered by station
    public FirestationResponseDTO getPersonsCoveredByStation(String stationNumber) throws IOException {

        logger.debug("Starting firestation response search for stationNumber={}", stationNumber);


        //récupérer les firestations et filtrer celles correspondant au numéro
        List<FirestationModel> firestations = firestationRepository.findAll();
        List<String> addresses = new ArrayList<>();
        for (FirestationModel f: firestations){
            if (f.getStation().equals(stationNumber)){
                addresses.add(f.getAddress());
            }
        }

        logger.debug("Found {} addresses covered by station {}", addresses.size(), stationNumber);


        //récupérer toutes les personnes et filtrer celles vivants aux addresses récupérées
        List<PersonModel> persons = personRepository.findAll();
        List<PersonFirestationDTO> personFirestationDTOS = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (PersonModel person: persons){
            if (addresses.contains(person.getAddress())){
                personFirestationDTOS.add(new PersonFirestationDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()));

                //comptage child/adult
                if (ageService.isAdult(person)){
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
