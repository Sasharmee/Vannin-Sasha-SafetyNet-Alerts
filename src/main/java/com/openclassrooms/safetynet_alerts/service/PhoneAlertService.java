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

@Service
public class PhoneAlertService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAlertService.class);

    //injection des deux repository car on va croiser les données
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    public PhoneAlertService(PersonRepository personRepository, FirestationRepository firestationRepository){
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }
    //on définit que la méthode doit renvoyer une liste de string selon les numéros de station
    public List<String> getPhoneByStation(String stationNumber) throws IOException{

        logger.debug("Starting phone alert search for stationNumber={}", stationNumber);

        //on récupére les firestations qu'on met dans une liste qui respecte notre modèle
        List<FirestationModel> firestations = firestationRepository.findAll();
        logger.debug("Loaded {} firestations", firestations.size());

        //on garde les adresses qui correspondent à la station renseignée
        List<String> addresses = new ArrayList<>();

        for (FirestationModel fs: firestations){
            if(fs.getAddress()!=null && fs.getStation().equals(stationNumber)){
                addresses.add(fs.getAddress());
            }
        }
        logger.debug("Found {} addresses covered by stationNumber={}", addresses.size(), stationNumber);

        //récupére les persons
        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons", persons.size());

        //création de la liste qui récupère les téléphones
        List<String> phones = new ArrayList<>();

        //si l'adresse de la person est couverte par la station
        for (PersonModel p: persons){
            if (p.getAddress()!=null && addresses.contains(p.getAddress())){
               //on ajoute son numéro
               if (p.getPhone()!=null){
                   phones.add(p.getPhone());
               }
            }
        }
        logger.debug("Phone alert search completed for stationNumber={}, phonesReturned={}",
                stationNumber, phones.size());

    return phones;
    }
}
