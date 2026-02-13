package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MedicalrecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalrecordService.class);

    private final PersonRepository personRepository;
    private MedicalrecordRepository medicalrecordRepository;

    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository, PersonRepository personRepository){
        this.medicalrecordRepository = medicalrecordRepository;
        this.personRepository = personRepository;
    }

    //GET
    public List<MedicalrecordModel> getAllMedicalrecord() throws IOException {
        logger.debug("Fetching all medicalrecords");

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        logger.debug("Found {} medicalrecords", medicalrecords.size());

        return medicalrecords;
    }


    //ADD
    public MedicalrecordModel addMedicalrecord(MedicalrecordModel medicalrecord) throws IOException{

        logger.debug("Adding medicalrecord for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean exists = medicalrecords.stream().anyMatch(m->m.getFirstName().equals(medicalrecord.getFirstName())&& m.getLastName().equals(medicalrecord.getLastName()));
        if (exists){
            logger.debug("Medicalrecord already exists for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());
            throw new IllegalArgumentException("Medicalrecord already exists");
        }
        medicalrecords.add(medicalrecord);
        medicalrecordRepository.saveAll(medicalrecords);

        logger.debug("Medicalrecord added successfully for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return medicalrecord;
    }

    //UPDATE
    public MedicalrecordModel updateMedicalrecord(MedicalrecordModel medicalrecord) throws IOException{

        logger.debug("Updating medicalrecord for {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for(MedicalrecordModel m: medicalrecords){
            if (m.getFirstName().equals(medicalrecord.getFirstName())&& m.getLastName().equals(medicalrecord.getLastName())) {
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

    //DELETE
    public boolean deleteMedicalrecord(String firstName, String lastName) throws IOException{

        logger.debug("Deleting medicalrecord for {} {}", firstName, lastName);


        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean removed  = medicalrecords.removeIf(m->m.getFirstName().equals(firstName)&&m.getLastName().equals(lastName));

        if (removed){
            medicalrecordRepository.saveAll(medicalrecords);
            logger.debug("Medicalrecord deleted successfully for {} {}", firstName, lastName);
        }else {
            logger.debug("No medicalrecord found to delete for {} {}", firstName, lastName);
        }
        return removed;
    }

}
