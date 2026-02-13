package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService service;

    public PersonController (PersonService service){
        this.service = service;

    }

    //GET, attention pas forcement nécessaire ici
    @GetMapping
    public List<PersonModel> getAllPersons() throws IOException {

        logger.info("GET /person called");

        List<PersonModel> result = service.getAllPersons();

        logger.info("GET /person success, {} persons returned", result.size());

        return result;
    }

    //POST
    @PostMapping
    public PersonModel addPerson(@RequestBody PersonModel person) throws IOException {

        logger.info("POST /person firstName={} lastName={}",
                person.getFirstName(), person.getLastName());

        PersonModel result = service.addPerson(person);

        logger.info("POST /person success for {} {}",
                person.getFirstName(), person.getLastName());

        return result;
    }

    //PUT
    @PutMapping
    public PersonModel updatePerson(@RequestBody PersonModel person) throws IOException {

        logger.info("PUT /person firstName={} lastName={}",
                person.getFirstName(), person.getLastName());

        PersonModel result = service.updatePerson(person);

        logger.info("PUT /person success for {} {}",
                person.getFirstName(), person.getLastName());

        return result;
    }

    //DELETE
    @DeleteMapping
    public boolean deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) throws IOException {
        logger.info("DELETE /person firstName={} lastName={}", firstName, lastName);

        boolean removed = service.deletePerson(firstName, lastName);

        logger.info("DELETE /person success removed={}", removed);

        return removed;
    }
}

