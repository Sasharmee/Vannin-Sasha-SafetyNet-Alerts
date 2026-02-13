package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.MedicalrecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AgeService {

    private static final Logger logger = LoggerFactory.getLogger(AgeService.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final int ADULT_AGE_THRESHOLD = 18;

    private final MedicalrecordRepository medicalrecordRepository;
    private final Clock clock;

    public AgeService(MedicalrecordRepository medicalrecordRepository, Clock clock){
        this.medicalrecordRepository=medicalrecordRepository;
        this.clock=clock;
    }

    public int calculateAge(PersonModel person) throws IOException {

        logger.debug("Calculating age for {} {}", person.getFirstName(), person.getLastName());

        List<MedicalrecordModel> medicalrecords = medicalrecordRepository.findAll();

        for (MedicalrecordModel medicalrecord: medicalrecords){
            if (medicalrecord.getFirstName().equalsIgnoreCase(person.getFirstName())&&medicalrecord.getLastName().equalsIgnoreCase(person.getLastName())){

                LocalDate birthdate = LocalDate.parse(medicalrecord.getBirthdate(), DATE_TIME_FORMATTER);
                LocalDate today = LocalDate.now(clock);

                int age = Period.between(birthdate, today).getYears();
                logger.debug("Age calculated for {} {} = {}", person.getFirstName(), person.getLastName(), age);

                return age;
            }
        }
        logger.debug("No medical record found for {} {}", person.getFirstName(), person.getLastName());
        return -1; //exception si person pas trouvée

    }

    public boolean isAdult(PersonModel person) throws IOException {
        boolean isAdult = this.calculateAge(person) > ADULT_AGE_THRESHOLD;
        logger.debug("{} {} isAdult={}", person.getFirstName(), person.getLastName(), isAdult);
        return isAdult;
    }

    public boolean isChild(PersonModel person) throws IOException{
        boolean isChild = this.calculateAge(person) <= ADULT_AGE_THRESHOLD;
        logger.debug("{} {} isChild={}", person.getFirstName(), person.getLastName(), isChild);
        return isChild;
    }

}
