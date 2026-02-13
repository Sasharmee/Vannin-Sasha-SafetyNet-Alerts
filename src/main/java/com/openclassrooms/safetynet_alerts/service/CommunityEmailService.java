package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityEmailService {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailService.class);

    private final PersonRepository personRepository;

    public CommunityEmailService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public List<String> getEmailsByCity(String city) throws IOException{

        logger.debug("Starting email search for city{}", city);

        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons from repository", persons.size());

        List<String> emails = new ArrayList<>();

        for (PersonModel person : persons) {
            if (person.getCity() != null && person.getCity().equalsIgnoreCase(city)) {
                if (person.getEmail() != null && !emails.contains(person.getEmail())) {
                    emails.add(person.getEmail());
                }
            }
        }
        logger.debug("Found {} unique emails for city{}", emails.size(), city);

        return emails;
    }
}
