package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository){
        this.firestationRepository = firestationRepository;
    }

    //ADD
    public List<FirestationModel> addFirestation(FirestationModel firestation) throws IOException{
        List<FirestationModel> firestations = firestationRepository.findAll();
        boolean exists = firestations.stream().anyMatch(f->f.getAddress().equals(firestation.getAddress()));
        if (exists){
            throw new IllegalArgumentException("address already covered by a station");
        }
        firestations.add(firestation);
        firestationRepository.saveAll(firestations);
        return firestations;
    }

    //UPDATE
    public List<FirestationModel> updateFirestation(FirestationModel firestation) throws IOException{
        List<FirestationModel> firestations = firestationRepository.findAll();

        for (FirestationModel f: firestations){
            if (f.getAddress().equals(firestation.getAddress())){
                f.setStation(firestation.getStation());
                firestationRepository.saveAll(firestations);
                return firestations;
            }
        }
        return null;
    }

    //DELETE
    public boolean deleteFirestation(String address, String station) throws IOException{
        List<FirestationModel> firestations = firestationRepository.findAll();

        boolean removed = firestations.removeIf(f ->
                (address != null && f.getAddress().equals(address)) ||
                        (station != null && f.getStation().equals(station)) //attention
        );

        if (removed){
            firestationRepository.saveAll(firestations);
        }
        return removed;
    }
}
