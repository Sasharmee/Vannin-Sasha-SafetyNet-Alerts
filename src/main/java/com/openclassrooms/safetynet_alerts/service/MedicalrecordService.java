package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MedicalrecordService {

    private final PersonRepository personRepository;
    private MedicalrecordRepository medicalrecordRepository;

    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository, PersonRepository personRepository){
        this.medicalrecordRepository = medicalrecordRepository;
        this.personRepository = personRepository;
    }

    //ADD
    public MedicalrecordModel addMedicalrecord(MedicalrecordModel medicalrecord) throws IOException{
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean exists = medicalrecords.stream().anyMatch(m->m.getFirstName().equals(medicalrecord.getFirstName())&& m.getLastName().equals(medicalrecord.getLastName()));
        if (exists){
            throw new IllegalArgumentException("Medicalrecord already exists");
        }
        medicalrecords.add(medicalrecord);
        medicalrecordRepository.saveAll(medicalrecords);
        return medicalrecord;
    }

    //UPDATE
    public MedicalrecordModel updateMedicalrecord(MedicalrecordModel medicalrecord) throws IOException{
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for(MedicalrecordModel m: medicalrecords){
            if (m.getFirstName().equals(medicalrecord.getFirstName())&& m.getLastName().equals(medicalrecord.getLastName())) {
                m.setBirthdate(medicalrecord.getBirthdate());
                m.setMedications(medicalrecord.getMedications());
                m.setAllergies(medicalrecord.getAllergies());
                medicalrecordRepository.saveAll(medicalrecords);
                return m;
            }
        }
        return null;
    }

    //DELETE
    public boolean deleteMedicalrecord(String firstName, String lastName) throws IOException{
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        boolean removed  = medicalrecords.removeIf(m->m.getFirstName().equals(firstName)&&m.getLastName().equals(lastName));

        if (removed){
            medicalrecordRepository.saveAll(medicalrecords);
        }
        return removed;
    }

}
