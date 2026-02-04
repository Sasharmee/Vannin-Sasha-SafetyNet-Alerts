package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AgeService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final int ADULT_AGE_THRESHOLD = 18;

    private final MedicalrecordRepository medicalrecordRepository;
    private final Clock clock;

    public AgeService(MedicalrecordRepository medicalrecordRepository, Clock clock){
        this.medicalrecordRepository=medicalrecordRepository;
        this.clock=clock;
    }

    public int calculateAge(PersonModel person) throws IOException {
        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for (MedicalrecordModel medicalrecord: medicalrecords){
            if (medicalrecord.getFirstName().equalsIgnoreCase(person.getFirstName())&&medicalrecord.getLastName().equalsIgnoreCase(person.getLastName())){

                LocalDate birthdate = LocalDate.parse(medicalrecord.getBirthdate(), DATE_TIME_FORMATTER);
                LocalDate today = LocalDate.now(clock);

                return Period.between(birthdate, today).getYears();
            }
        }
        return -1; //exception si person pas trouvée

    }

    public boolean isAdult(PersonModel person) throws IOException {
        return this.calculateAge(person) > ADULT_AGE_THRESHOLD;
    }

    public boolean isChild(PersonModel person) throws IOException{
        return this.calculateAge(person)<= ADULT_AGE_THRESHOLD;

    }

}
