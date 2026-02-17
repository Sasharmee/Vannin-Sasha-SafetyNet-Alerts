package com.openclassrooms.safetynet_alerts.service;



import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final AgeService ageService;

    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository, AgeService ageService){
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.ageService = ageService;
    }

    //GET
    public List<FirestationModel> getAllFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Fetching all firestations");

        List<FirestationModel> firestations = firestationRepository.findAll();

        logger.debug("Found {} firestations", firestations.size());

        return firestations;
    }

    //ADD
    public List<FirestationModel> addFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Adding firestation for address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> firestations = firestationRepository.findAll();
        boolean exists = firestations.stream().anyMatch(f->f.getAddress().equals(firestation.getAddress()));
        if (exists){
            logger.debug("Firestation already exists for address={}", firestation.getAddress());
            throw new IllegalArgumentException("address already covered by a station");
        }
        firestations.add(firestation);
        firestationRepository.saveAll(firestations);

        logger.debug("Firestation added successfully, total firestations={}", firestations.size());

        return firestations;
    }

    //UPDATE
    public List<FirestationModel> updateFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Updating firestation for address={} newStation={}",
                firestation.getAddress(), firestation.getStation());


        List<FirestationModel> firestations = firestationRepository.findAll();

        for (FirestationModel f: firestations){
            if (f.getAddress().equals(firestation.getAddress())){
                f.setStation(firestation.getStation());
                firestationRepository.saveAll(firestations);

                logger.debug("Firestation updated for address={}", firestation.getAddress());
                return firestations;
            }
        }
        logger.debug("No firestation found for address={}", firestation.getAddress());
        return null;
    }

    //DELETE
    public boolean deleteFirestation(String address, String station) throws IOException{

        logger.debug("Deleting firestation with address={} or station={}", address, station);

        List<FirestationModel> firestations = firestationRepository.findAll();

        boolean removed = firestations.removeIf(f ->
                (address != null && f.getAddress().equals(address)) ||
                        (station != null && f.getStation().equals(station)) //attention
        );

        if (removed){
            logger.debug("Firestation deleted successfully");
            firestationRepository.saveAll(firestations);
        }else {
            logger.debug("No firestation matched deletion criteria");
        }
        return removed;
    }

}
