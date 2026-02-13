package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoService.class);

    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;
    private final AgeService ageService;

    public PersonInfoService(PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, AgeService ageService){
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.ageService = ageService;
    }

    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) throws IOException {

        logger.debug("Starting to search personInfo for lastName: {}", lastName);

        List<PersonModel> persons = personRepository.findAll();
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        logger.debug("Loaded {} persons and {} medicalrecords", persons.size(), medicalrecords.size());

        List<PersonInfoDTO> result = new ArrayList<>();

        for (PersonModel person : persons) {

            if (person.getLastName() == null || !person.getLastName().equalsIgnoreCase(lastName)) {
                continue;
            }

            int age = ageService.calculateAge(person);

            // chercher medical record
            MedicalrecordModel foundMedical = null;
            for (MedicalrecordModel mr : medicalrecords) {
                boolean samePerson =
                        mr.getFirstName().equalsIgnoreCase(person.getFirstName())
                                && mr.getLastName().equalsIgnoreCase(person.getLastName());

                if (samePerson) {
                    foundMedical = mr;
                    break;
                }
            }

            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (foundMedical != null) {
                if (foundMedical.getMedications() != null) medications = foundMedical.getMedications();
                if (foundMedical.getAllergies() != null) allergies = foundMedical.getAllergies();
            }


            PersonInfoDTO dto = new PersonInfoDTO(
                    person.getLastName(),
                    person.getAddress(),
                    age,
                    person.getEmail(),
                    medications,
                    allergies
            );

            result.add(dto);
        }

        logger.debug("Personinfo search completed for lastName={}, results={}", lastName, result.size());

        return result;
    }
}