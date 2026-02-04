package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildAlertService {

    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;

    public ChildAlertService(PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository){
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
    }

    public List<ChildAlertDTO> getChildrenByAddress(String address){
        List<PersonModel> persons
    }
}
