package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FloodService {

    private static final Logger logger = LoggerFactory.getLogger(FloodService.class);

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalrecordRepository medicalrecordRepository;
    private final AgeService ageService;

    public FloodService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalrecordRepository medicalrecordRepository, AgeService ageService){
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.ageService = ageService;
    }
    //Stations = string du type "1,2"
    public List<FloodDTO> getFloodByStation(String stations) throws IOException {
        //Transformer String stations en une list
        String[] stationNumbers = stations.split(",");

        //Trouver address des stations
        logger.debug("Starting flood search for stations={}", stations);

        List<FirestationModel> firestations = firestationRepository.findAll();
        List<String> addresses = new ArrayList<>();

        for (FirestationModel fs: firestations){
            for (String station: stationNumbers){
                String trimmed = station.trim();
                if (fs.getStation()!=null && fs.getStation().equals(trimmed)){
                    String address = fs.getAddress();
                    if (address != null && !addresses.contains(address)){
                        addresses.add(address);
                    }
                }
            }
        }
        logger.debug("Found {} addresses for stations={}", addresses.size(), stations);

        //Préparer un groupement par adresse (clé = address, valeur = list de persons)
        Map<String, List<ResidentInfoDTO>> residentByAddress = new HashMap<>();
        for (String address : addresses){
            residentByAddress.put(address, new ArrayList<>());
        }

        //Récuperer persons et medicalrecords
        List<PersonModel> persons = personRepository.findAll();
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        logger.debug("Loaded {} persons and {} medicalrecords", persons.size(), medicalrecords.size());

        //récuperer persons par address pour construire DTO
        for (PersonModel person: persons){
            String personAddress = person.getAddress();
            if (personAddress == null){
                continue;
            }
            //adress de la personne dans la liste
            if (!addresses.contains(personAddress)){
                continue;
            }

            //age de la person
            int age = ageService.calculateAge(person);

            //récupérer medicalrecord de la person
            MedicalrecordModel foundMedical = null;
            for (MedicalrecordModel medicalrecord : medicalrecords){
                boolean samePerson = medicalrecord.getFirstName().equalsIgnoreCase(person.getFirstName())&&medicalrecord.getLastName().equalsIgnoreCase(person.getLastName());
            if (samePerson){
                foundMedical = medicalrecord;
                }
            }

            //récupérer medications et allergies
            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (foundMedical != null){
                if (foundMedical.getMedications()!=null) medications = foundMedical.getMedications();
                if (foundMedical.getAllergies()!=null) allergies = foundMedical.getAllergies();
            }

            //création DTO resident

            ResidentInfoDTO resident = new ResidentInfoDTO(person.getLastName(), person.getPhone(), age, medications, allergies);

            residentByAddress.get(personAddress).add(resident);

        }

        //Transformer map en list
        List<FloodDTO> result = new ArrayList<>();

        for (String address : addresses){
            List<ResidentInfoDTO> residents = residentByAddress.get(address);
            if (residents!=null&&!residents.isEmpty()){
                result.add(new FloodDTO(address, residents));
            }
        }

        logger.debug("Flood search completed for stations={}, householdsReturned={}", stations, result.size());


        return result;
    }
}
